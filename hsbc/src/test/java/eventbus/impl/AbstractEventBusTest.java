package eventbus.impl;

import eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractEventBusTest {

    protected EventBus eventBus;

    @BeforeEach
    public abstract void setUpBase();

    @Test
    public void testAddSubscriberAndPublish() {
        CountDownLatch latch = new CountDownLatch(1);
        MessageSubscriber messageSubscriber = Mockito.spy(new MessageSubscriberTest(latch));
        eventBus.addSubscriber(MessageEvent.class, messageSubscriber);

        eventBus.publishEvent(new MessageEvent("HelloTestMessage"));

        try {
            latch.await();
            Mockito.verify(messageSubscriber, times(1)).onEvent(Mockito.any());
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    public void testPublishWithoutSubscriber() {
        eventBus.publishEvent(new MessageEvent(("NoSubscriber")));
    }

}
