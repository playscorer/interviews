package eventbus.impl.multi.conflated;

import eventbus.impl.MessageEvent;
import eventbus.impl.MessageSubscriberTest;
import eventbus.impl.multi.MultiThreadEventBusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atMost;

public class ConflateEventBusTest extends MultiThreadEventBusTest {

    @BeforeEach
    public void setUpBase() {
        eventBus = new ConflatedEventBus();
    }

    @Test
    public void testPublishManyIdenticalMessages() {
        CountDownLatch latch = new CountDownLatch(1);
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));
        ((ConflatedEventBus) eventBus).startConsumers();

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(messageEvent);
        }

        try {
            latch.await();
            Mockito.verify(messageEvent, atMost(10)).getMessage();
        } catch (InterruptedException e) {
            fail(e);
        }

    }

}
