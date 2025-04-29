package org.example.eventbus.waiting;

import java.util.concurrent.atomic.AtomicLong;

public class SpinYieldStrategy implements WaitStrategy {
    private WaitInfoSource source;
    private long waitTime;

    private long spinCount = 1000;

    private AtomicLong lastSeqId = new AtomicLong(-1);

    public SpinYieldStrategy(WaitInfoSource signal) {
        this.source = signal;
        this.waitTime = 100;
    }

    @Override
    public void performWait() {
        // spin waiting for new data to be delivered, no locking
        for (long i1=0; i1<=spinCount; ++i1) {
            long pos = source.getSequenceId();
            long last = lastSeqId.get();
            boolean changed = last != pos;
            if (changed) {
                lastSeqId.set(pos);
                return;
            }
        }

        // at this point, no change, so we perform a context switch wait...
        final var closeable = this.source.registerWaiting(); // inform we have a waiter
        try (closeable) {
            Object signal = source.getSignal();
            synchronized (signal) {
                try {
                    signal.wait(waitTime);
                    lastSeqId.set(source.getSequenceId()); // for our next spin
                } catch (InterruptedException e) {
                    // exit
                }
            }
        } catch (Exception e) {
        }
    }
}
