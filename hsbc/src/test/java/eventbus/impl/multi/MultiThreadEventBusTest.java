package eventbus.impl.multi;

import eventbus.impl.AbstractEventBusTest;
import eventbus.impl.MessageEvent;
import eventbus.impl.MessageSubscriber;
import eventbus.impl.MessageSubscriberTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class MultiThreadEventBusTest extends AbstractEventBusTest {

    @BeforeEach
    public void setUpBase() {
        eventBus = new MultiThreadEventBus();
    }

    @Test
    public void testPublishOnceToTwoSubscribers() {
        CountDownLatch latch = new CountDownLatch(2);
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));
        eventBus.publishEvent(messageEvent);
        try {
            latch.await();
            Mockito.verify(messageEvent, times(2)).getMessage();
        } catch (InterruptedException e) {
            fail(e);
        }

    }


    @Test
    public void testPublishMultipleTimesToOneSubscriber() {
        CountDownLatch latch = new CountDownLatch(100);
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(messageEvent);
        }

        try {
            latch.await();
            Mockito.verify(messageEvent, times(100)).getMessage();
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void testPublishMultipleTimesToMultipleSubscribers() {
        CountDownLatch latch = new CountDownLatch(10*100);
        for (int i = 0; i < 10; i++) {
            eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));
        }

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(messageEvent);
        }

        try {
            latch.await();
            Mockito.verify(messageEvent, times(10*100)).getMessage();
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void testFilteredMessages() {
        CountDownLatch latch = new CountDownLatch(20);
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriberTest(latch));
        eventBus.addSubscriberForFilteredEvents(MessageEvent.class, new MessageSubscriberTest(latch), event -> false);

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("FilteredMessage"));

        for (int i = 0; i < 10; i++) {
            eventBus.publishEvent(messageEvent);
        }

        try {
            latch.await();
            Mockito.verify(messageEvent, times(10)).getMessage();
        } catch (InterruptedException e) {
            fail(e);
        }
    }
}
