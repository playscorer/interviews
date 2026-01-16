package barclays;

import java.util.*;

/**
 * Given this code, answer the question marks:
 *
 * public class StringWrapper {
 *     private String data;
 *     public StringWrapper(String data) {
 *         this.data = data;
 *     }
 *     public String getData() {
 *         return data;
 *     }
 *
 *     public void setData(String data) {
 *         this.data = data;
 *     }
 *
 *     @Override
 *     public boolean equals(Object o) {
 *         if (this == o) return true;
 *         if (o == null || getClass() != o.getClass()) return false;
 *         StringWrapper that = (StringWrapper) o;
 *         return Objects.equals(data, that.data);
 *     }
 *     @Override
 *     public int hashCode() {
 *         return Objects.hash(data);
 *     }
 * }
 * ----
 * public void mapTest() {
 *     final Map<StringWrapper, String> dataMap = new HashMap<>();
 *     StringWrapper wrapper1 = new StringWrapper("one");
 *     StringWrapper wrapper2 = new StringWrapper("two");
 *     StringWrapper wrapper3 = new StringWrapper("three");
 *     dataMap.put(wrapper1, "1");
 *     dataMap.put(wrapper2, "2");
 *     dataMap.put(wrapper3, "3");
 *     System.out.println(wrapper1.equals(wrapper3)); //?
 *      wrapper1.setData("three");
 *      System.out.println(wrapper1.hashCode()); //?
 *      System.out.println(wrapper3.hashCode()); //?
 * 	System.out.println(wrapper1.equals(wrapper3)); //?
 * 	System.out.println(dataMap.get(wrapper1));
 * }
 *
 * How to make it immutable? the field data also needs to be immutable for reflection, clarity, and thread-safety, final class
 * and remove the setter
 *
 * How to use computeIfAbsent?
 * dataMap.computeIfAbsent(2026, k -> new ArrayList<>()).add("January");
 *
 * All ways to iterate through a collection? 2 type of for loops, iterators, stream(), forEach()
 *
 * What happens if trying to remove an element while iterating through the collection?
 * ConcurrentModificationException -> use iterator.remove()
 *
 * Can you override a static method? No but you can hide it.
 *
 * What is reflection? reflection is invaluable in scenarios where the structure of the code isn't known until the program is running.
 * Use case is also accessing a private field in a test.
 * It breaks Java principles (encapsulation, performance, and code maintainability).
 *
 * Collections (Maps vs Set vs Lists - contract between hashcode & equals)
 *
 * Concurrent modifiers (synchronized, volatile)
 *
 * What does this code snippet do?
 *
 * Runnable r = new Runnable() {
 *                @Override
 *        public void run() {
 * 			System.out.println("hello");
 *        }    * 	};
 * 	r.run();
 *
 * 	Thread t = new Thread(r);
 * 	t.run();
 * 	t.start();
 *
 * Exceptions vs Runtime Exceptions (unchecked) vs Errors (Errors and Exceptions are subclasses of Throwable) - Errors should not be caught (JVM internal state error)
 *
 * Runnable vs Callable
 *
 * CAS (atomicity)
 *
 * ThreadLocal (each thread accesses its own variable, provides thread safety without synchronization)
 *
 * Spring events
 *
 * Component vs Service -  All @Services are Components, but not all Components are @Services. You use @Component for general beans and @Service (or @Repository for data access, @Controller for presentation) to be more specific about a bean's role.
 *
 * Can you mix xml and annotations? Yes
 *
 * Type of injections? Constructor vs Field vs Setter
 *
 * Scopes? Singleton (default) vs Prototype
 *
 * Difference type of JOINS - INNER vs OUTER (LEFT, RIGHT, FULL)
 *
 * What is Sharding?
 *
 * TRUNC VS DROP VS DELETE (needs commit)
 *
 * Gradle Core Concepts
 *
 * How to iterate through a single linked list once pass and find the mid element?
 * Use two pointers (slow moves by one, and faster moves by two).
 *
 * Your input is a list of dishes, where each dish is associated with its ingredients.
 * Please implement an algorithm to group dishes that have at least one common ingredient.
 *
 * Example input:
 *
 * "Pasta" -> ["Tomato Sauce", "Onions", "Garlic"]
 *
 * "Chicken Curry" -> ["Chicken", "Curry Sauce"]
 *
 * "Fried Rice" -> ["Rice", "Onions", "Nuts"]
 *
 * "Salad" -> ["Spinach", "Nuts"]
 *
 * "Sandwich" -> ["Cheese", "Bread"]
 *
 * "Quesadilla" -> ["Chicken", "Cheese"]
 *
 * Output:
 *
 * ("Pasta", "Fried Rice")
 *
 * ("Fried Rice", "Salad")
 *
 * ("Chicken Curry", "Quesadilla")
 *
 * ("Sandwich", "Quesadilla")
 *
 */
public class QuantitativeISScreening {
    //
}

final class StringWrapper {
    private String data;

    public StringWrapper(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringWrapper that = (StringWrapper) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public static void main(String[] args) {
        final Map<StringWrapper, String> dataMap = new HashMap<>();
        StringWrapper wrapper1 = new StringWrapper("one");
        StringWrapper wrapper2 = new StringWrapper("two");
        StringWrapper wrapper3 = new StringWrapper("three");
        dataMap.put(wrapper1, "1");
        dataMap.put(wrapper2, "2");
        dataMap.put(wrapper3, "3");
        System.out.println(wrapper1.equals(wrapper3)); //?
        wrapper1.setData("three");
        System.out.println(wrapper1.hashCode()); //?
        System.out.println(wrapper3.hashCode()); //?
        System.out.println(wrapper1.equals(wrapper3)); //?
        System.out.println(dataMap.get(wrapper1));

        // my addition
        System.out.println(wrapper2.hashCode());
        wrapper2.setData("four");
        System.out.println(wrapper2.hashCode());
        System.out.println(dataMap.get(wrapper2));

        // "Pasta" -> ["Tomato Sauce", "Onions", "Garlic"]
        // "Chicken Curry" -> ["Chicken", "Curry Sauce"]
        // "Fried Rice" -> ["Rice", "Onions", "Nuts"]
        // "Salad" -> ["Spinach", "Nuts"]
        // "Sandwich" -> ["Cheese", "Bread"]
        // "Quesadilla" -> ["Chicken", "Cheese"]
        Map<String, List<String>> ingredientsMap = new HashMap<>();
        ingredientsMap.put("Pasta", Arrays.asList("Chicken", "Onions", "Garlic"));
        ingredientsMap.put("Chicken Curry", Arrays.asList("Chicken", "Curry Sauce"));
        ingredientsMap.put("Fried Rice", Arrays.asList("Rice", "Onions", "Nuts"));
        ingredientsMap.put("Salad", Arrays.asList("Spinach", "Nuts"));
        ingredientsMap.put("Sandwich", Arrays.asList("Cheese", "Bread"));
        ingredientsMap.put("Quesadilla", Arrays.asList("Chicken", "Cheese"));

        // ("Pasta", "Fried Rice")
        // ("Fried Rice", "Salad")
        // ("Chicken Curry", "Quesadilla")
        // ("Sandwich", "Quesadilla")
        Map<String, List<String>> dishesMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : ingredientsMap.entrySet()) {
            entry.getValue().forEach(v -> dishesMap.computeIfAbsent(v, k-> new ArrayList<>()).add(entry.getKey()));
        }
        //dishesMap.forEach((k,v) -> System.out.println(k + " " + dishesMap.get(k)));
        dishesMap.values().stream().filter(l -> l.size() > 1).forEach(System.out::println);
    }
}