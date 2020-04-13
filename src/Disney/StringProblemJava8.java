package Disney;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StringProblemJava8 {

    public static TreeMap<Character, List<Integer>> constructMap(String str) {
	return IntStream.range(0, str.length()).boxed()
		.collect(Collectors.toMap(i -> Character.valueOf(str.charAt(i)), i -> Collections.singletonList(i),
			(i1, i2) -> Stream.concat(i1.stream(), i2.stream()).collect(Collectors.toList()), TreeMap::new));
    }
    
    public static void displayMap(TreeMap<Character, List<Integer>> sortedMap) {
	sortedMap.entrySet().stream().flatMap(x -> x.getValue().stream().map(y -> x.getKey() + ": " + y))
		.forEach(System.out::println);
    }

    public static void main(String[] args) {
	TreeMap<Character, List<Integer>> sortedMap = StringProblemJava8.constructMap("hello");
	displayMap(sortedMap);
    }
    
}
