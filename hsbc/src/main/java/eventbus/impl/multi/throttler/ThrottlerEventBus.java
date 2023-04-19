package eventbus.impl.multi.throttler;

import eventbus.Event;
import eventbus.Throttler;
import eventbus.impl.multi.MultiThreadEventBus;

import java.util.concurrent.TimeUnit;

public class ThrottlerEventBus extends MultiThreadEventBus {

    private final Throttler throttler;

    public ThrottlerEventBus(long maxRequests, long timeWindow, TimeUnit timeUnit) {
        throttler = new ThrottlerImpl(maxRequests, timeWindow, timeUnit);
    }

    public ThrottlerEventBus(Throttler throttler) {
        this.throttler = throttler;
    }

    @Override
    public void publishEvent(Event event) {
        if (Throttler.ThrottleResult.DO_NOT_PROCEED.equals(throttler.shouldProceed())) {
            throttler.notifyWhenCanProceed();
        }
        super.publishEvent(event);
    }

}
