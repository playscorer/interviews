package java8.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Iterator {
    
    public static void show(List<String> list) {
	list.forEach(System.out::println);
    }
    
    public static void showPair(List<String> list) {
	IntStream.range(0, list.size())
		.mapToObj(i -> String.format("%d: %s", i, list.get(i)))
		.forEach(System.out::println);
    }

    public static void main(String[] args) {
	List<String> list = new ArrayList<>();
	list.add("One");
	list.add("Two");
	list.add("Three");
	
	Iterator.show(list);
	System.out.println();
	Iterator.showPair(list);
    }
    
}
