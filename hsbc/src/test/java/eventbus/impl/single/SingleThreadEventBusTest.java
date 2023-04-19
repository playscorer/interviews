package eventbus.impl.single;

import eventbus.impl.AbstractEventBusTest;
import eventbus.impl.MessageEvent;
import eventbus.impl.MessageSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SingleThreadEventBusTest extends AbstractEventBusTest {

    @BeforeEach
    public void setUpBase() {
        eventBus = new SingleThreadEventBus();
    }

    @Test
    public void testPublishOnceToTwoSubscribers() {
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriber());
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriber());

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));
        eventBus.publishEvent(messageEvent);

        Mockito.verify(messageEvent, times(2)).getMessage();
    }


    @Test
    public void testPublishMultipleTimesToOneSubscriber() {
        eventBus.addSubscriber(MessageEvent.class, new MessageSubscriber());

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(messageEvent);
        }

        Mockito.verify(messageEvent, times(100)).getMessage();
    }

    @Test
    public void testPublishMultipleTimesToMultipleSubscribers() {
        for (int i = 0; i < 10; i++) {
            eventBus.addSubscriber(MessageEvent.class, new MessageSubscriber());
        }

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("HelloTestMessage"));

        for (int i = 0; i < 100; i++) {
            eventBus.publishEvent(messageEvent);
        }

        Mockito.verify(messageEvent, times(100*10)).getMessage();
    }

    @Test
    public void testFilteredMessages() {
        eventBus.addSubscriberForFilteredEvents(MessageEvent.class, new MessageSubscriber(), event -> false);

        MessageEvent messageEvent = Mockito.spy(new MessageEvent("FilteredMessage"));
        eventBus.publishEvent(messageEvent);

        Mockito.verify(messageEvent, never()).getMessage();
    }
}
