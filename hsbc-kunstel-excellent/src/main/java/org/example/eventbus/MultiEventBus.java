package org.example.eventbus;

import org.example.eventbus.consumer.EventHandler;
import org.example.eventbus.consumer.ExceptionHandler;
import org.example.eventbus.consumer.Subscription;
import org.example.eventbus.waiting.SpinYieldStrategy;
import org.example.eventbus.waiting.WaitInfoSource;
import org.example.eventbus.waiting.WaitStrategy;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;

public class MultiEventBus<T> implements WaitInfoSource {

    private final ConcurrentHashMap<Long, Subscription<T>> subscribers = new ConcurrentHashMap<>();
    private final AtomicLong subscriptionCount = new AtomicLong();

    private AtomicReferenceArray<T> ringBuffer;
    private int ringBufferSize;

    private int skipSafetyBuffer = 2;

    private AtomicLong writeIndex = new AtomicLong(); // the next place a writer will add to ringBuffer
    private AtomicLong completedWriteIndex = new AtomicLong(); // the last place successfully written to ringbuffer

    private Object signal = new Object();

    private ExceptionHandler<T> onException; // if set, any issues may be delivered here

    private AtomicLong waitingReaders = new AtomicLong();

    private boolean provideSimpleBackPressure = true;

    // Constructor, takes a parameter for the max size of the ring buffer
    public MultiEventBus(int maxBufferSize) {
        this.ringBufferSize = maxBufferSize;
        ringBuffer = new AtomicReferenceArray<>(maxBufferSize);
        this.provideSimpleBackPressure = true;
    }

    // register a subscription with the event bus, returning a subscription id that can be later unsubscribed
    public Subscription<T> register(Subscription<T> subscriber) {
        long id = subscriptionCount.getAndIncrement();
        subscriber.setSubscriptionId(id);
        subscriber.getReadSequenceNumber().set(completedWriteIndex.get());
        subscribers.put(id, subscriber);
        return subscriber;
    }

    // create a subscriber to events, which will call a simple callback function (lamda) on update
    public Subscription<T> register(EventHandler<T> callback) {
        var sub = new Subscription<T>();
        sub.setCallback(callback);
        return register(sub);
    }

    // unsubscribe to feed based on subscription id
    public boolean unregister(long id) {
        return subscribers.remove(id) != null;
    }

    // given a previous subscription, unsubscribe to events
    public boolean unregister(Subscription<T> sub) {
        return subscribers.remove(sub.getSubscriptionId()) != null;
    }

    // publish event onto the ringbuffer
    // with backpressure off and no-readers in a wait state, this is lock free
    // (two atomic increments and a vol write)
    public long publishEvent(T event) {
        if (provideSimpleBackPressure) {
            checkBackPressure();
        }

        var seq = writeIndex.getAndIncrement();
        var index = seq % ringBufferSize;
        ringBuffer.set((int)index, event);
        completedWriteIndex.incrementAndGet();

        // in case we have other reader threads in a yield wait etc, we need to signal new data
        if (waitingReaders.get() >= 0) {
            synchronized (signal) {
                signal.notifyAll();
            }
        }
        return seq;
    }

    // simple backpressure implementation - we check if any subscribers have fallen too far behind
    // (and about to lose data) - if so we thread yield until subscribers have caught up
    protected void checkBackPressure() {
        // scan through subscribers and see where they are in their reading...
        while (true) {
            var iterator = subscribers.entrySet().iterator();
            long smallest = writeIndex.get();
            while (iterator.hasNext()) { // iterate through ConcurrentHashMap
                smallest = Math.min(smallest, iterator.next().getValue().getReadSequenceNumber().get());
            }
            var backlog = (writeIndex.get() - smallest) + skipSafetyBuffer + 1;
            if (backlog < ringBufferSize) {
                return; // no issue, all subscribers have caught up to the data
            }
            Thread.yield(); // one or more subscribers are behind and are about to miss data, yield and recheck
        }
    }

    // function used for manually performing callback operations
    // for all subscribers, retrieves/sends the next event
    // returns true if events were pumped/something was done
    public boolean pump() {
        boolean performedWork = false;
        var iterator = subscribers.entrySet().iterator();
        long currentIndex = completedWriteIndex.get();
        while (iterator.hasNext()) {
            var subscription = iterator.next().getValue();
            long seqId = subscription.getReadSequenceNumber().get(); // atomic operators to see if this may be out of date
            if (seqId != currentIndex) {
                // may be out of date
                performedWork = true;
                attemptWork(subscription); // will attempt to process work here, unless there is another thread already doing it
            }
        }
        return performedWork || currentIndex != completedWriteIndex.get();
    }

    private boolean attemptWork(Subscription<T> subscription) {
        Exception issue = null;
        var lock = subscription.getLock();
        if (!lock.tryLock()) {
            return false; // someone else has lock, exit
        }
        try {
            long seqId = subscription.getReadSequenceNumber().get(); // get subscribers seq id
            T item = ringBuffer.get((int)(seqId % this.ringBufferSize)); // get data it would map to (right or wrong)

            long currentWriteIndex = writeIndex.get(); // get the current write location

            // check if we have overflowed - if so, "item" is wrong, so we alert we've overflowed
            // and move our sequence numbers forward
            if ((currentWriteIndex-seqId) >= (ringBufferSize-1)) { // check if we have overflowed, pass back error
                long mx = Math.max(seqId+1, (currentWriteIndex-ringBufferSize) + skipSafetyBuffer);
                issue = new IllegalStateException("Could not keep up with writer, lost " + (mx-seqId) + " packets");
                subscription.getReadSequenceNumber().set(mx);
            } else {
                // normal case - lets check if it is appropriate to callback with data
                long completed = completedWriteIndex.get();
                if (seqId < completed) {
                    subscription.getReadSequenceNumber().incrementAndGet();
                    var callback = subscription.getCallback();
                    var matcher = subscription.getMatcher();
                    if (callback != null && matcher == null || matcher.canHandle(item)) {
                        callback.onEvent(item, seqId);
                    }
                }
            }
        } catch (Exception ex) {
            issue = ex; // exception thrown - pass back to our logging system...
        }
        finally {
            lock.unlock();
        }
        if (issue != null) {
            // if issues were found, perform exception callbacks if necessary
            if (onException != null) {
                onException.onException(issue, subscription);
            }
            if (subscription.getOnException() != null) {
                subscription.getOnException().onException(issue, subscription);
            }
        }
        return true;
    }

    // used to create worker threads which will background process/pump events
    // uses by default SpinYield strategy to wait for updates
    public Function<Boolean, Thread[]> createWorkerThreads(int threadCount) {
        return createWorkerThreads(threadCount, new SpinYieldStrategy(this));
    }

    // create worker threads to pump/comsume events
    // completed can change asynschroneously true->false, stopping thread processing
    public Function<Boolean, Thread[]> createWorkerThreads(int threadCount, WaitStrategy waitStrategy) {
        var r = new ArrayList<Thread>();
        var outer = this;
        AtomicBoolean forceStop = new AtomicBoolean(false);
        AtomicBoolean processingCompleted = new AtomicBoolean(false);
        Phaser p = new Phaser(threadCount+1);

        // create thread to pump events in the background...
        for (int i1=0; i1<threadCount; ++i1) {
            Thread thread = new Thread(() -> {
                p.arrive();
                while (!processingCompleted.get()) {
                    while (!forceStop.get() && outer.pump()); // keep pumping events
                    if (processingCompleted.get()) {
                        return;
                    }
                    waitStrategy.performWait();
                }
            });
            r.add(thread);
            thread.start();
        }
        p.arrive();
        // at this point all threads have started
        var ret = r.toArray(new Thread[0]);
        // return a function that can be used to stop the background threads when param is true
        return doForceStop -> {
            processingCompleted.set(true);
            forceStop.set(doForceStop);
            synchronized (signal) {
                signal.notifyAll();
            }
            // wait for threads to complete
            for (int i1=0; i1<ret.length; ++i1) {
                try {
                    ret[i1].join();
                } catch (InterruptedException e) {}
            }
            return ret;
        };
    }

    @Override
    public Object getSignal() {
        return signal;
    }

    @Override
    public long getSequenceId() {
        return completedWriteIndex.get();
    }

    private final AutoCloseable descrementReader = () -> waitingReaders.decrementAndGet();

    @Override
    public AutoCloseable registerWaiting() { // WaitInfoSource
        waitingReaders.incrementAndGet();
        return descrementReader;
    }

    public void setProvideSimpleBackPressure(boolean provideSimpleBackPressure) {
        this.provideSimpleBackPressure = provideSimpleBackPressure;
    }
}
