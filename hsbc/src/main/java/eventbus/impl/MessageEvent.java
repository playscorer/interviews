package eventbus.impl;

import eventbus.Event;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageEvent extends Event<String> {

    public MessageEvent(String message) {
        super(message);
    }

    public MessageEvent(AtomicInteger conflationKey, String message) {
        super(conflationKey, message);
    }
}
