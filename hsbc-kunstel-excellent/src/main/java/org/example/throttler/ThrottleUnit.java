package org.example.throttler;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class ThrottleUnit implements AutoCloseable, Comparable<ThrottleUnit> {
    private static final AtomicLong nextId = new AtomicLong();

    private final long id;
    private final Consumer<ThrottleUnit> callback;
    private final long startTime;
    private long endTime;

    public ThrottleUnit(Consumer<ThrottleUnit> callback, long startTime, long endTime) {
        this.id = nextId.incrementAndGet();
        this.callback = callback;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    @Override
    public void close() throws Exception {
        this.callback.accept(this);
    }

    @Override
    public int compareTo(ThrottleUnit other) {
        if (this.endTime != other.endTime) {
            return Long.compare(this.endTime, other.endTime);
        } else {
            return Long.compare(this.id, other.id);
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
