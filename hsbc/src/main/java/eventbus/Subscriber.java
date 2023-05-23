package eventbus;

public interface Subscriber {

    void onEvent(Event event) throws EventException;

    void setEventFilter(EventFilter eventFilter);
}
