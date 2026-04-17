package millennium;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 4/16/26 - Screening with Kesavan Thirucherai
 *
 * I was asked which version of SpringBoot I worked with? Spring Boot 3.0 in 2023
 */
public class Round1 {

    static class Point {
        int x;
        int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
    }

    static record Person (String name, String department, double sales) {}

    public static void main(String[] args) {
        double a = 0.9 - 0.8;
        double b = 0.8 - 0.7;

        System.out.println(a);
        System.out.println(b);
        System.out.println(a == b); // Always false because of floating-point precision problem

        // solution
        BigDecimal aA = new BigDecimal("0.9").subtract(new BigDecimal("0.8"));
        BigDecimal bB = new BigDecimal("0.8").subtract(new BigDecimal("0.7"));
        System.out.println(aA.compareTo(bB) == 0);  // true

        // immutable object
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 10, 0);
        modify(time);
        System.out.println(time);

        // solution
        time = modifySolution(time);
        System.out.println(time);

        /**
         * String Pool
         * Java maintains a string pool (part of the heap) where string literals are interned
         * meaning identical literals share the same object.
         */
        String stra = "hello"; // stored in pool
        String strb = "hello"; // reuses same object from pool
        System.out.println(stra == strb); // true — same reference

        // new String("hello") bypasses the pool and forces a new object on the heap
        System.out.println(new String("hello").equals("hello")); // true
        System.out.println(new String("hello") == "hello");      // false

        /*
        Java is always pass-by-value. But what gets passed depends on the type:
            Primitives (int, double, etc.) — the value itself is copied. Changes inside a method don't affect the caller.
            Objects — the reference is copied (not the object). So you can mutate the object's fields,
            but you can't make the caller's variable point to a different object.
         */

        /*
         Java tries to unbox x — i.e. call x.intValue() — to convert Integer to int.
         But x is null, so calling a method on it throws NPE.
         */
        //Integer x = null;
        //int y = x;

        /*
        The difference comes down to **how each collection finds elements**.
        `HashSet` locates elements by computing `hashCode()` first, then checking `equals()`.
        `Point`'s `hashCode()` is based on its coordinates, so when `p1` was added,
        it was bucketed under hash of `(1,1)`.
        After mutating `x=2`, the hash is now `(2,1)` — a different bucket.
        The set looks in the wrong bucket and finds nothing → `false`.

        `ArrayList` ignores `hashCode()` entirely.
        `contains()` just walks the array calling `equals()` on each element until
        it finds a match → `true`.

        The root problem: you mutated an object that's being used as a hash key.
        This is undefined behavior for hash-based collections
        the contract requires that `hashCode()` doesn't change while the object is in the set.

        Rule: never mutate fields that contribute to `hashCode()` while the object lives in a `HashSet` or `HashMap`.
         */
        List<Point> list = new ArrayList<>();
        Set<Point> set = new HashSet<>();
        Point p1 = new Point(1, 1);
        set.add(p1);
        list.add(p1);
        p1.x = 2;

        System.out.println(set.contains(p1)); // false
        System.out.println(list.contains(p1)); // true

        /**
         * Return the person having made the max in sales.
         * Return the department having made the max in sales.
         *
         * Java and SQL
         *
         * D1 -> A1 250, A2 350 -> 600
         * D2 -> B1 55, B2 75 -> 130
         */
        List<Person> people = List.of(new Person("A1", "D1", 250),
                new Person("A2", "D1", 350),
                new Person("B1", "D2", 55),
                new Person("B2", "D2", 75));

        // Person having done max sales (I wanted to use a PriorityQueue by offering each element at a time)
        // this is an overkill for a one-time max — O(n log n) vs O(n) for old school method or heapify
        // offer & poll are O(log n) - n being the size of the heap
        // heapify - new PriorityQueue(collection) is O(n), but adding n elements one by one via offer() is O(n log n)
        Person person = people.stream()
                .max(Comparator.comparingDouble(Person::sales))
                .orElseThrow();
        System.out.println("Max Person Sales: " + person);
        /*
        SELECT *
            FROM Person
            WHERE sales = (SELECT MAX(sales) FROM Person);
         */

        // Dept having done max sales
        String dept = people.stream()
                .collect(Collectors.groupingBy(Person::department,
                        Collectors.summingDouble(Person::sales))) // {"D1": 600.0, "D2": 130.0}
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();
        System.out.println("Max Dept Sales: " + dept);

        /*
        SELECT department, SUM(sales) AS total_sales -- summingDouble(Person::sales)
            FROM Person
            GROUP BY department         -- groupingBy(Person::department)
            ORDER BY total_sales DESC   -- max(Map.Entry.comparingByValue())
            LIMIT 1;                    --
         */

        /*
         Using the MAX function in a subquery
         *
        SELECT department
            FROM (
                SELECT department, SUM(sales) AS total_sales
                FROM Person
                GROUP BY department
            ) AS dept_totals
            WHERE total_sales = (SELECT MAX(total_sales)
                                 FROM (SELECT SUM(sales) AS total_sales
                                       FROM Person
                                       GROUP BY department) AS sub);
         */

        /*
         Old School 1
         *
         *
            Map<String, Double> mapSales = new HashMap<>();
            for (Person p : people) {
                mapSales.merge(p.department(), p.sales(), Double::sum);
            }

            String maxDept = null;
            double maxSales = Double.MIN_VALUE;
            for (Map.Entry<String, Double> entry : mapSales.entrySet()) {
                if (entry.getValue() > maxSales) {
                    maxSales = entry.getValue();
                    maxDept = entry.getKey();
                }
            }
        */

        /*
         Old School 2
         *
            // Step 1 - group
            Map<String, List<Person>> mapSales = new HashMap<>();
            for (Person p : people) {
                mapSales.computeIfAbsent(p.department(), k -> new ArrayList<>()).add(p);
            }

            // Step 2 - sum each group
            Map<String, Double> totals = new HashMap<>();
            for (Map.Entry<String, List<Person>> entry : mapSales.entrySet()) {
                double sum = 0;
                for (Person p : entry.getValue()) {
                    sum += p.sales();
                }
                totals.put(entry.getKey(), sum);
            }

            // Step 3 - find max
            String maxDept = null;
            double maxSales = Double.MIN_VALUE;
            for (Map.Entry<String, Double> entry : totals.entrySet()) {
                if (entry.getValue() > maxSales) {
                    maxSales = entry.getValue();
                    maxDept = entry.getKey();
                }
            }
         */
    }

    private static void modify(LocalDateTime t) {
        t.plusDays(1);
        t.plusHours(2);
        System.out.println(t);
    }

    private static LocalDateTime modifySolution(LocalDateTime t) {
        t = t.plusDays(1);
        t = t.plusHours(2);
        System.out.println(t);
        return t;
    }
}
