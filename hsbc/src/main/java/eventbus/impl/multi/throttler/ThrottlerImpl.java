package eventbus.impl.multi.throttler;

import eventbus.Throttler;

import java.util.concurrent.TimeUnit;

public class ThrottlerImpl implements Throttler {
    private final long maxRequests;
    private final long timeWindow;
    private final TimeUnit timeUnit;
    private long lastRequestTime;
    private long requestCount;

    public ThrottlerImpl(long maxRequests, long timeWindow, TimeUnit timeUnit) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindow;
        this.timeUnit = timeUnit;
        this.lastRequestTime = System.currentTimeMillis();
    }

    @Override
    public synchronized ThrottleResult shouldProceed() {
        long now = System.currentTimeMillis();

        if (requestCount < maxRequests) {
            requestCount++;
            lastRequestTime = now;
            return ThrottleResult.PROCEED;
        }
        return ThrottleResult.DO_NOT_PROCEED;
    }

    @Override
    public void notifyWhenCanProceed() {
        long now = System.currentTimeMillis();
        long waitTime = timeUnit.toMillis(timeWindow) - (now - lastRequestTime);
        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        requestCount = 1;
        lastRequestTime = System.currentTimeMillis();
    }
}
