package eventbus.impl;

import eventbus.Event;
import lombok.Data;

import java.util.concurrent.CountDownLatch;

@Data
public class MessageSubscriberTest extends MessageSubscriber {

    private CountDownLatch latch;

    public MessageSubscriberTest(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onEvent(Event event) {
        super.onEvent(event);
        latch.countDown();
    }

}
