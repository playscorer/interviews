package java8.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Aggregate {
    
    public static int maxOf(int[] array) {
	return Arrays.stream(array).max().getAsInt();
    }
    
    public static int maxOf(List<Integer> list) {
	return list.stream().max(Comparator.comparing(Function.identity())).get();
    }
    
    public static int maxOfv2(List<Integer> list) {
	return list.stream().mapToInt(x -> x).max().getAsInt();
    }

    public static void main(String[] args) {
	int[] array = {8, 2, 3, 7, 18, -3};
	List<Integer> list = Arrays.stream(array).mapToObj(Integer::new).collect(Collectors.toList());
	
	System.out.println(String.format("Max is: %d", maxOf(array)));
	System.out.println(String.format("Max is: %d", maxOf(list)));
	System.out.println(String.format("Max is: %d", maxOfv2(list)));
    }

}
