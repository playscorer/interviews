package java8.lambda;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FlatMap {

    public static void main(String[] args) {
        int[] intArray = {1, 2, 3, 4, 5, 6};
        Arrays.stream(intArray).mapToObj(i -> i + " ").forEach(System.out::print);

        //1. Stream<int[]>
        Stream<int[]> streamArray = Stream.of(intArray);

        //2. Stream<int[]> -> flatMap -> IntStream
        IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));
        
        System.out.println();
        intStream.forEach(System.out::println);
    }

}
