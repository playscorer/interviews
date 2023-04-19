package eventbus;

public interface Subscriber {

    void onEvent(Event event);

    void setEventFilter(EventFilter eventFilter);
}
