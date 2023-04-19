package eventbus.impl.single;

import eventbus.Event;
import eventbus.EventBus;
import eventbus.EventFilter;
import eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleThreadEventBus implements EventBus {

    private final Map<Class<?>, List<Subscriber>> subscribersByType = new HashMap<>();

    @Override
    public void publishEvent(Event event) {
        List<Subscriber> subscribers = subscribersByType.get(event.getClass());
        if (subscribers != null) {
            subscribers.forEach(subscriber -> subscriber.onEvent(event));
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
