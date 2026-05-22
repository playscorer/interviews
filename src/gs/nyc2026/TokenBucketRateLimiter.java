package gs.nyc2026;/*
 * Click `Run` to execute the snippet below!
 */

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Jeffersonton, VA - Round 2 of Super Day - May 19th, 2026
 * Solution: A high-performance, thread-safe Rate Limiter.
 */
public class TokenBucketRateLimiter {
    // TODO Define internal state variables here (e.g., capacity, refill rate, timestamps)
    private static final long SCALE = 1000L; // to avoid loss of precision in the refill
    // refillRatePerSecond × 1e-9 × 1000 >= 1
    // refillRatePerSecond >= 1e6 tokens/second
    // So SCALE=1000 only preserves precision for refill rates above 1 million tokens/second.

    private final long capacity;
    private final double refillRatePerSecond;
    private final AtomicLong currentTokens;
    private final AtomicLong lastRefillTimestamp;

    /**
     * Initializes the rate limiter.
     * 
     * @param capacity            The maximum burst size (maximum tokens in the bucket).
     * @param refillRatePerSecond The rate at which tokens are replenished.
     */
    public TokenBucketRateLimiter(long capacity, double refillRatePerSecond) {
        // TODO: Initialize the rate limiter state
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.currentTokens = new AtomicLong(capacity * SCALE);
        this.lastRefillTimestamp = new AtomicLong(System.nanoTime());
    }

    /**
     * Evaluates if a single request should be allowed based on the rate limit.
     * 
     * @return true if the request is permitted, false if it should be throttled.
     */
    public boolean allowRequest() {
        return allowRequest(1);
    }

    /**
     * Evaluates if a batch of requests should be allowed.
     * 
     * @param tokens The number of tokens/permits to consume for this request.
     * @return true if the permits are successfully consumed, false otherwise.
     */
    public boolean allowRequest(int tokens) {
        // TODO

        // calculate elapsed time and refill the bucket since last request
        // elapsedTime = now - lastRefillTimestamp;
        // refilled = elapsedTime in sec * refillRatePerSecond
        long now = System.nanoTime();
        long lastTs = lastRefillTimestamp.get();
        long refill = (long) Math.min(capacity * SCALE, (now - lastTs) * 1e-9 * refillRatePerSecond * SCALE);

        // best effort refill - if another thread already refilled, skip
        // Under high contention only one thread refills per period
        long current = currentTokens.get();
        long updated = Math.min(capacity * SCALE, current + refill);
        if (currentTokens.compareAndSet(current, updated)) {
            lastRefillTimestamp.compareAndSet(lastTs, now);
        }

        // consume
        // check remaining capacity is enough
        // decrement by number tokens
        do {
            current = currentTokens.get();
            if (current < (long) tokens * SCALE) return false;
            if (currentTokens.compareAndSet(current, current - tokens * SCALE)) return true;
        } while (true);
    }


    /**
     * Main method for local verification and unit testing.
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Example: 10 tokens capacity, 5 tokens per second refill rate
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(10, 5.0);
        
        // Test logic here TODO
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.scheduleAtFixedRate(() -> {
            boolean allowed = limiter.allowRequest();
            System.out.println(Thread.currentThread().getName() + ": " + allowed);
        }, 0, 200, TimeUnit.MILLISECONDS);

        executor.scheduleAtFixedRate(() -> {
            boolean allowed = limiter.allowRequest();
            System.out.println(Thread.currentThread().getName() + ": " + allowed);
        }, 0, 200, TimeUnit.MILLISECONDS);

        // let it run for 3 seconds then stop
        Thread.sleep(3000);
        executor.shutdown();

        //System.out.println("Request 1: " + limiter.allowRequest());
    }
}