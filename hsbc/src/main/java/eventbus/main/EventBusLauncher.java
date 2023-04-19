package eventbus.main;

import eventbus.EventBus;
import eventbus.impl.MessageEvent;
import eventbus.impl.MessageSubscriber;
import eventbus.impl.Producer;
import eventbus.impl.multi.conflated.ConflatedEventBus;
import eventbus.impl.multi.MultiThreadEventBus;
import eventbus.impl.single.SingleThreadEventBus;
import eventbus.impl.multi.throttler.ThrottlerEventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class EventBusLauncher {

    public static void main(String[] args) throws InterruptedException {
        // Single Threaded Event Bus
        log.info("Single Threaded Event Bus");
        EventBus eventBus = new SingleThreadEventBus();
        launchEventBus(eventBus);

        Thread.sleep(2000);

        // Multithreaded Event Bus
        log.info("Multithreaded Event Bus");
        eventBus = new MultiThreadEventBus();
        launchEventBus(eventBus);

        Thread.sleep(2000);

        // Conflated Event Bus
        log.info("Conflated Event Bus");
        eventBus = new ConflatedEventBus();
        launchConflatedEventBus((ConflatedEventBus) eventBus);

        Thread.sleep(2000);

        // Throttler Event Bus
        log.info("Throttler Event Bus");
        eventBus = new ThrottlerEventBus(100, 1, TimeUnit.SECONDS);
        launchThrottlerEventBus((ThrottlerEventBus) eventBus);
    }

    private static void launchEventBus(EventBus eventBus) {
        MessageSubscriber consumer1 = new MessageSubscriber();
        eventBus.addSubscriber(MessageEvent.class, consumer1);

        MessageSubscriber consumer2 = new MessageSubscriber();
        eventBus.addSubscriber(MessageEvent.class, consumer2);

        MessageSubscriber consumer3 = new MessageSubscriber();
        eventBus.addSubscriberForFilteredEvents(MessageEvent.class, consumer3, event -> false);

        Producer producer1 = new Producer(eventBus);
        producer1.postEvent(new MessageEvent("Message from producer #1"));
        Producer producer2 = new Producer(eventBus);
        producer2.postEvent(new MessageEvent("Message from producer #2"));
        Producer producer3 = new Producer(eventBus);
        producer3.postEvent(new MessageEvent("Message from producer #3"));
    }

    private static void launchConflatedEventBus(ConflatedEventBus eventBus) {
        MessageSubscriber consumer = new MessageSubscriber();
        eventBus.addSubscriber(MessageEvent.class, consumer);
        eventBus.startConsumers();

        for (int i=0; i<10; i++) {
            new Producer(eventBus).postEvent(new MessageEvent(new AtomicInteger(1), "Message from producer " + i));
        }
    }

    private static void launchThrottlerEventBus(ThrottlerEventBus eventBus) {
        MessageSubscriber consumer1 = new MessageSubscriber();
        eventBus.addSubscriber(MessageEvent.class, consumer1);

        MessageSubscriber consumer2 = new MessageSubscriber();
        eventBus.addSubscriber(MessageEvent.class, consumer2);

        MessageSubscriber consumer3 = new MessageSubscriber();
        eventBus.addSubscriberForFilteredEvents(MessageEvent.class, consumer3, event -> false);

        for (int i=0; i<5000; i++) {
            new Producer(eventBus).postEvent(new MessageEvent("Message from producer " + i));
        }
    }

}
