package eventbus.impl;

import eventbus.Event;
import eventbus.EventFilter;
import eventbus.Subscriber;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public class MessageSubscriber implements Subscriber {

    private EventFilter eventFilter;

    @Override
    public void onEvent(Event event) {
        if (eventFilter == null || eventFilter.test(event)) {
            log.info("Event processed: {} by: {}", event.getMessage(), Thread.currentThread().getName());
        }
    }
}
