package eventbus.impl.multi.throttler;

import eventbus.Throttler;
import eventbus.impl.MessageEvent;
import eventbus.impl.MessageSubscriberTest;
import eventbus.impl.multi.MultiThreadEventBusTest;
import eventbus.impl.multi.throttler.ThrottlerEventBus;
import eventbus.impl.multi.throttler.ThrottlerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.times;

public class ThrottlerEventBusTest extends MultiThreadEventBusTest {

    @Test
    public void testAboveMaxMessages() {
        Throttler throttler = Mockito.spy(new ThrottlerImpl(10, 1, TimeUnit.SECONDS));
        eventBus = new ThrottlerEventBus(throttler);

        CountDownLatch latch = new CountDownLatch(100);
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(new MessageEvent("HelloTestMessage"));
        }

        try {
            latch.await();
            Mockito.verify(throttler, times(100)).shouldProceed();
            Mockito.verify(throttler, atMost(10)).notifyWhenCanProceed();
        } catch (InterruptedException e) {
            fail(e);
        }
    }

}
