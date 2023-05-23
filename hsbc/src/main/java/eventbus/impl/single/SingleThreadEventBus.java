package eventbus.impl.single;

import eventbus.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SingleThreadEventBus implements EventBus {

    private final Map<Class<?>, List<Subscriber>> subscribersByType = new HashMap<>();

    @Override
    public void publishEvent(Event event) {
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
