package citi.prep2026;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Key design decisions to remember:
 *
 * record State — immutable, no setters, fields final by design
 * AtomicReference<State> — single atomic swap of both fields together
 * while(true) CAS loop — retry on contention, exit only on success or exhaustion
 * maxTokens - 1 on window reset — consume one token for current request immediately
 * Parameterized — maxTokens and windowNanos passed in, not hardcoded
 *
 * Common follow-ups:
 *
 * Multiple token cost per request — allowRequest(int tokens) checks tokenCount >= tokens, decrements by tokens
 * Distributed rate limiter — AtomicReference won't work across JVMs, need Redis with Lua scripts
 * Burst allowance — allow short bursts above rate by tracking both burst bucket and sustained rate
 */
public class RateLimiter {

    record State(long tokenCount, long windowStart) {}

    private final AtomicReference<State> state;
    private final long maxTokens;
    private final long windowNanos;

    public RateLimiter(long maxTokens, long windowNanos) {
        this.maxTokens = maxTokens;
        this.windowNanos = windowNanos;
        this.state = new AtomicReference<>(new State(maxTokens, System.nanoTime()));
    }

    public boolean allowRequest() {
        while (true) {
            State s = state.get();
            long now = System.nanoTime();

            State newState;
            if (now - s.windowStart >= windowNanos) {
                // window expired — reset and consume one token
                newState = new State(maxTokens - 1, now);
            } else if (s.tokenCount > 0) {
                // tokens available — consume one
                newState = new State(s.tokenCount - 1, s.windowStart);
            } else {
                // no tokens left
                return false;
            }

            if (state.compareAndSet(s, newState)) {
                return true;
            }
            // CAS failed — another thread modified state, retry
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 100 requests per 10 seconds
        RateLimiter limiter = new RateLimiter(100, 10_000_000_000L);

        ExecutorService pool = Executors.newFixedThreadPool(20,
            new ThreadFactory() {
                private final AtomicInteger count = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("requester-thread-" + count.incrementAndGet());
                    return t;
                }
            }
        );

        for (int i = 0; i < 200; i++) {
            pool.submit(() -> {
                boolean allowed = limiter.allowRequest();
                System.out.println(Thread.currentThread().getName()
                    + " → " + (allowed ? "ALLOWED" : "REJECTED"));
            });
        }

        pool.shutdown();
        pool.awaitTermination(60, TimeUnit.SECONDS);
    }
}