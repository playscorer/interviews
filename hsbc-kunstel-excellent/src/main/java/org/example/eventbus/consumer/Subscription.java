package org.example.eventbus.consumer;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class Subscription<T> {
    private Matcher<T> matcher;
    private EventHandler callback;

    private ExceptionHandler onException;

    private long subscriptionId;

    private AtomicLong sequenceNumber = new AtomicLong();

    private ReentrantLock lock = new ReentrantLock();

    public Subscription() {
    }

    public Subscription(EventHandler<T> callback) {
        this.callback = callback;
    }

    public Subscription(EventHandler<T> callback, Matcher<T> matcher) {
        this.callback = callback;
        this.matcher = matcher;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public AtomicLong getReadSequenceNumber() {
        return sequenceNumber;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public EventHandler getCallback() {
        return callback;
    }

    public void setCallback(EventHandler callback) {
        this.callback = callback;
    }

    public Matcher<T> getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public ExceptionHandler getOnException() {
        return onException;
    }

    public void setOnException(ExceptionHandler onException) {
        this.onException = onException;
    }
}
