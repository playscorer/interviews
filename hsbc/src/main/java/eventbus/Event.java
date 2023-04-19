package eventbus;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Event<T> {

    // for conflation, we want any event with same key in the queue to be replaced
    private AtomicInteger conflationKey = new AtomicInteger();

    @EqualsAndHashCode.Exclude
    private T message;

    public Event(T message) {
        conflationKey.incrementAndGet();
        this.message = message;
    }

}
