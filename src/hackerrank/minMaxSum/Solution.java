package hackerrank.minMaxSum;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

/**
 * Given five positive integers, find the minimum and maximum values that can be calculated by summing exactly four of the five integers.
 * Then print the respective minimum and maximum values as a single line of two space-separated long integers.
 *
 * Example arr = [1,3,5,7,9]
 *
 * The minimum sum is 1 + 3 + 5 + 7 = 16 and the maximum sum is 3 + 5 + 7 + 9 = 24.
 * The function prints 16 24
 *
 * Params: arr: an array of 5 integers
 * Prints: Print two space-separated integers on one line: the minimum sum and the maximum sum of 4 of 5 elements.
 *
 * Input Format
 *  A single line of five space-separated integers.
 *
 * Output Format
 *  Print two space-separated long integers denoting the respective minimum and maximum values that can be calculated by summing exactly four of the five integers.
 *  (The output can be greater than a 32 bit integer.)
 */
class Result {

    /*
     * Complete the 'miniMaxSum' function below.
     *
     * The function accepts INTEGER_ARRAY arr as parameter.
     */

    public static void miniMaxSum(List<Integer> arr) {
    // Write your code here
        Collections.sort(arr);
        
        long min = IntStream.range(0, 4).mapToLong(arr::get).sum();
        long max= IntStream.range(1,5).mapToObj(arr::get)
                .collect(Collectors.summingLong(Integer::longValue));
        
        System.out.println(min + " " + max);
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        Result.miniMaxSum(arr);

        bufferedReader.close();
    }
}
