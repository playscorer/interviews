package org.example.eventbus.waiting;

public interface WaitInfoSource {

    public Object getSignal();
    public long getSequenceId();

    public AutoCloseable registerWaiting();
}
