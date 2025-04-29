package org.example.throttler;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RateLimiter implements Throttleable, AutoCloseable {

    private TreeSet<ThrottleUnit> requestHistory;
    private Deque<CompletableFuture<ThrottleUnit>> waiters = new ArrayDeque<>(100);

    private int maxItems;
    private long msTimePeriod;

    private long nextWakeUpTime = Long.MAX_VALUE;

    private Thread backgroundThread;

    protected static final long MIN_CLEANUP_DELAY_MS = 1;

    private volatile boolean running = true;

    public RateLimiter(int maxItems, long msTimePeriod) {
        this.requestHistory = new TreeSet<>();
        this.maxItems = maxItems;
        this.msTimePeriod = msTimePeriod;
        backgroundThread = new Thread(this::wakeupTask);
        backgroundThread.setPriority(Thread.NORM_PRIORITY+1);
        backgroundThread.start();
    }

    @Override
    public synchronized ThrottleUnit tryTake() {
        var ret = getNext();
        return ret instanceof ThrottleUnit ? (ThrottleUnit) ret : null;
    }

    // either return a ThrottleUnit if we can immediately return, or a long saying the next wakeup time
    private Object getNext() {
        long now = getCurrentTimeMs();

        long endTime = getDefaultEndTime(now);
        Long smallestWakeupTime = cleanToNext();

        // see if we can immediately add it
        if (requestHistory.size() < maxItems) {
            ThrottleUnit unit = new ThrottleUnit(callback, now, endTime);
            requestHistory.add(unit);
            return unit;
        }
        // if here, we just have to wait... return the smallestWakeupTime
        return smallestWakeupTime == null ? now + msTimePeriod : smallestWakeupTime;
    }

    private final Consumer<ThrottleUnit> callback = throttleUnit -> onCompleted(throttleUnit);

    protected synchronized void onCompleted(ThrottleUnit throttleUnit) {
        if (requestHistory.remove(throttleUnit)) {
            long now = getCurrentTimeMs();
            throttleUnit.setEndTime(now);
            requestHistory.add(throttleUnit);
            long ms = now + msTimePeriod;
            if (ms < nextWakeUpTime) {
                nextWakeUpTime = ms;
                this.notifyAll();
            }
        }
    }

    protected long getDefaultEndTime(long nowTime) {
        return nowTime + Integer.MAX_VALUE;
    }

    // return a completableFuture that completes on the next available timeslice, returning a ThrottleUnit
    public synchronized CompletableFuture<ThrottleUnit> takeNext() {
        var next = getNext();
        if (next instanceof ThrottleUnit) { // can immediately get it
            return CompletableFuture.completedFuture((ThrottleUnit) next);
        }

        CompletableFuture<ThrottleUnit> future = new CompletableFuture<>();
        waiters.addLast(future);
        return future;
    }

    protected synchronized void retrievePending(Map<ThrottleUnit, CompletableFuture<ThrottleUnit>> ret) {
        ret.clear();

        if (!waiters.isEmpty()) {
            // check if we can schedule any waiting completable futures now
            do {
                var next = getNext();
                if (next instanceof ThrottleUnit) {
                    var future = waiters.removeFirst();
                    ret.put((ThrottleUnit) next, future);
                } else {
                    break;
                }
            } while (!waiters.isEmpty());
        }
    }

    protected void wakeupTask() {
        var toProcess = new TreeMap<ThrottleUnit, CompletableFuture<ThrottleUnit>>();
        while (running) {
            houseKeeping(toProcess);
            // remove any timedout units
            synchronized (this) {
                Long nextWakeup = cleanToNext();
                long now = getCurrentTimeMs();

                // now do a wait for the next cleanup time
                long next = requestHistory.isEmpty() || nextWakeup == null ? now + 10000 : nextWakeup;
                long msWait = next - now;
                try {
                    if (msWait > 0 && running) {
                        this.wait(Math.max(msWait, MIN_CLEANUP_DELAY_MS)); // this gives up the lock...
                    }
                } catch (InterruptedException e) {
                    /* ingore */
                }
            }
        }
    }

    // clean old, and anything pending given a chance
    protected void houseKeeping(TreeMap<ThrottleUnit, CompletableFuture<ThrottleUnit>> toProcess) {
        retrievePending(toProcess);
        // done outside synchronized block as completion may have (slow) side effects
        if (!toProcess.isEmpty()) {
            for (var entry : toProcess.entrySet()) {
                entry.getValue().complete(entry.getKey());
            }
        }
    }

    private Long cleanToNext () {
        long now = getCurrentTimeMs();
        long pastTime = now - this.msTimePeriod;

        Iterator<ThrottleUnit> iter = requestHistory.iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            if (entry.getEndTime() <= pastTime) {
                iter.remove();
            } else {
                nextWakeUpTime = requestHistory.first().getEndTime();
                return nextWakeUpTime;
            }
        }

        return null;
    }

    // set the wakeup at this time in the future

    protected long getCurrentTimeMs() {
        return System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
        running = false;
        synchronized (this) {
            this.notifyAll();
            requestHistory.clear();
            InterruptedException ex = new InterruptedException("Rate Limiter closed");
            for (var w : waiters) {
                w.completeExceptionally(ex);
            }
            waiters.clear();
        }
    }

}
