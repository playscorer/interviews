package hackerrank.lonelyInteger;

import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Given an array of integers, where all elements but one occur twice, find the unique element.
 *
 * Example a = [1,2,3,4,3,2,1]
 * The unique element is 4.
 *
 * Params: int a[n]: an array of integers
 * Returns int: the element that occurs only once
 *
 * Input Format
 *  The first line contains a single integer, n, the number of integers in the array.
 *  The second line contains  space-separated integers that describe the values in a.
 *
 * Constraints
 *  It is guaranteed that n is an odd number and that there is one unique element.
 */
class Result {

    /*
     * Complete the 'lonelyinteger' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts INTEGER_ARRAY a as parameter.
     */

    public static int lonelyinteger(List<Integer> a) {
        // 2 * sum (unique elements) - sum (all elements)
        return (new HashSet<Integer>(a).stream().mapToInt(Integer::intValue).sum()) * 2 - a.stream().mapToInt(Integer::intValue).sum();

        // naive solution
        /*
            Collections.sort(a);
            Integer[] array = a.toArray(new Integer[a.size()]);
            int i=0;
            while (i<a.size()-1) {
                if (array[i] == array[i+1]) {
                    i++;
                }
                if ((i + 1) % 2 == 0) {
                    i++;
                }
            }

            return array[i];
        */
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        int result = Result.lonelyinteger(a);

        System.out.println(result);

        bufferedReader.close();
    }
}
