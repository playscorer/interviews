package eventbus.impl.multi;

import eventbus.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MultiThreadEventBus implements EventBus {

    private final Map<Class<?>, List<Subscriber>> subscribersByType = Collections.synchronizedMap(new HashMap<>());

    // to insure ordering
    // private final Map<Class<?>, Queue> queueByType;

    private ExecutorService executor
            = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            0L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void publishEvent(Event event) {
        executor.execute(() -> {
            List<Subscriber> subscribers = subscribersByType.get(event.getClass());
            if (subscribers != null) {
                subscribers.forEach(subscriber -> {
                    try {
                        subscriber.onEvent(event);
                    } catch (EventException e) {
                        log.error(e.getMessage());
                    }
                });
            }
        });
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

}
