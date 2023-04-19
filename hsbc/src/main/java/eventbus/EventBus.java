package eventbus;

public interface EventBus {

    /**
     * Sends an event to the bus.
     */
    void publishEvent(Event event);

    /**
     * Register subscriber as an event consumer to the event bus.
     */
    void addSubscriber(Class<? extends Event> eventType, Subscriber subscriber);

    /**
     * Register subscriber as a filtered event consumer to the event bus.
     */
    void addSubscriberForFilteredEvents(Class<? extends Event> eventType, Subscriber subscriber, EventFilter eventFilter);

}
