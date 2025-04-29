package org.example.eventbus.consumer;

public interface Matcher<T> {
    boolean canHandle(T event);
}
