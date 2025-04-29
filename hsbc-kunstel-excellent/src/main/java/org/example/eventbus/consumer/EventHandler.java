package org.example.eventbus.consumer;

public interface EventHandler<T> {
    void onEvent(T event, long sequenceNumber);
}
