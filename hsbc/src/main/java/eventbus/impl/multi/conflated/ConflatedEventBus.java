package eventbus.impl.multi.conflated;

import eventbus.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConflatedEventBus implements EventBus {

    private final Map<Class<?>, List<Subscriber>> subscribersByType = Collections.synchronizedMap(new HashMap<>());

    private final BlockingQueue<Event> eventCache = new ArrayBlockingQueue(100);

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void publishEvent(Event event) {
        if (eventCache.contains(event)) { // very time costly option
            eventCache.remove(event);
        }
        eventCache.offer(event);
    }

    @Override
    public void addSubscriber(Class<? extends Event> eventType, Subscriber subscriber) {
        subscribersByType.computeIfAbsent(eventType, x -> new ArrayList<>()).add(subscriber);
    }

    @Override
    public void addSubscriberForFilteredEvents(Class<? extends Event> eventType, Subscriber subscriber, EventFilter eventFilter) {
        subscriber.setEventFilter(eventFilter);
        subscribersByType.computeIfAbsent(eventType, x -> new ArrayList<>()).add(subscriber);
    }

    public void startConsumers() {
        for (int i=0; i<Runtime.getRuntime().availableProcessors(); i++) {
            executor.execute(() -> {
                while (true) {
                    subscribersByType.keySet().forEach(aClass -> {
                        List<Subscriber> subscribers = subscribersByType.get(aClass);
                        if (subscribers != null) {
                            try {
                                Event event = eventCache.take();
                                subscribers.forEach(subscriber -> {
                                    try {
                                        subscriber.onEvent(event);
                                    } catch (EventException e) {
                                        log.error(e.getMessage());
                                    }
                                });

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                }
            });
        }
    }

}
