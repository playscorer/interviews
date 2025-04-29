package org.example.eventbus.consumer;

public interface ExceptionHandler<T> {
    public void onException(Exception ex, Subscription<T> sub);
}
