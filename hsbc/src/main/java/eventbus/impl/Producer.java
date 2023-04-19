package eventbus.impl;

import eventbus.Event;
import eventbus.EventBus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Producer {

    private EventBus eventBus;

    public void postEvent(Event event) {
        eventBus.publishEvent(event);
    }

}
