package barclays.equities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Given the following code, write the customSort method signature and body (takes a generic list) :
 *
 *    List<User> users = Arrays.asList(
 *         new User("Alice", 30),
 *         new User("Bob", 12),
 *         new User("Charlie", 25),
 *         new User("Dave", 40)
 *     );
 *
 *     List<User> sortedByAge = customSort(users, User::age);
 *     List<User> sortedByNameLength = customSort(users, u -> u.name().length());
 *
 * The Difference Between map() and flatMap()
 *
 * Difference between Concurrency and Parallelism
 *
 * How to make an Integer thread safe? AtomicInteger
 *
 * Type erasure at runtime
 *
 * Difference between <? extends T> and <? super T>
 *
 * Does this code compile?
 *         List<? extends Number> nums = new ArrayList<Integer>();
 *         Number n = nums.get(0);
 *         nums.add(10);
 *
 * Spring @AutoEnabledConfig
 *
 * Spring application startup events are fired in a defined, predictable sequence.
 *
 * Spring events are synchronous, meaning the publisher's thread waits for all listeners to finish processing the event before continuing,
 * but you can easily make them asynchronous using the @Async annotation on the listener method.
 *
 * Spring Webflux
 *
 * VirtualThreads Java 21
 *
 * Zookeeper (Apache ZooKeeper) is a centralized service for distributed applications that provides essential coordination, configuration,
 * and naming services, acting like a reliable "phonebook" or "single source of truth" and "lock" for all the different parts (nodes or servers)
 * of a distributed system, helping them work together seamlessly, even if some parts fail.
 *
 * Sharding, also known as horizontal partitioning, involves dividing a large database into smaller, more manageable databases, or 'shards'.
 * Each shard is a distinct database instance.
 *
 * Oracle SQL is the choice for established enterprise applications where data integrity (ACID compliance)
 * and complex relationships between structured data are paramount.
 *
 * MongoDB is favored by developers for modern applications that need flexibility, fast development cycles,
 * and the ability to scale horizontally with large volumes of semi-structured or unstructured data.
 *
 * Kdb+ is a highly specialized, niche product used in industries like finance for its extreme speed in handling time-series data analysis
 * and high-frequency operations.
 *
 * Maven phases
 */
public class Screening {

    public record User(String name, Integer age) {}

    private static <U> List<U> customSort(List<U> users, Function<U, Integer> keyExtractor) {
        // my wrong solution : list.sorted(criteria).collect(Collectors.toList())
        users.sort(Comparator.comparing(keyExtractor));
        return users;
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User("Alice", 30),
                new User("Bob", 12),
                new User("Charlie", 25),
                new User("Dave", 40)
        );

        List<User> sortedByAge = customSort(users, User::age);
        List<User> sortedByNameLength = customSort(users, u -> u.name().length());

        /*
            PECS: Producer Extends, Consumer Super - tells you which wildcard to use:
            ? extends T — you READ from it (producer)
            ? super T — you WRITE to it (consumer)
         */

        // You can GET out a Number safely. You cannot PUT in because the compiler doesn't know if it's a List<Integer>, List<Double>, or List<Float>.
        List<? extends Number> nums = new ArrayList<Integer>();
        //Number n = nums.get(0);
        // it does not compile - Java doesn't know the exact type, so it prevents all additions
        //nums.add(10);

        // list could be
        new ArrayList<Double>();
        new ArrayList<Long>();

        // You can PUT in an Integer safely because whatever the list holds (Integer, Number, or Object) can always accept an Integer.
        // But when you GET out, all the compiler can guarantee is Object.
        List<? super Integer> ints = new ArrayList<Number>();
        ints.add(2);
        // does not compile
        //ints.add(2d);

        ints.forEach(System.out::println);

        // list could be
        new ArrayList<Integer>();
        new ArrayList<Number>();
        new ArrayList<Object>();
    }

}
