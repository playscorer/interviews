package org.example.throttler;

import java.util.concurrent.CompletableFuture;

public interface Throttleable {
    // simple case, return a throttle unit if we are allowed to proceed, null if we are not
    // ThrottleUnit MUST be closed once action is complete (it is autocloseable)
    ThrottleUnit tryTake();

    // return a completableFuture that completes on the next available timeslice
    CompletableFuture<ThrottleUnit> takeNext();
}
