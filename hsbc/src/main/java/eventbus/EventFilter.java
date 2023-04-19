package eventbus;

import java.util.function.Predicate;

public interface EventFilter extends Predicate<Event> {
}
