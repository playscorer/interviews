package DSA;

import java.util.Arrays;

/**
 * https://www.interviewcake.com/concept/java/counting-sort
 */
public class CountingSort {

    public static void main(String[] args) {
        int[] sorted = sort(new int[] {3, 5, 1, 0, 10, 2});
        Arrays.stream(sorted).forEach(System.out::println);
    }

    public static int[] sort(int[] theArray) {
        int maxValue = 0;
        for (int item : theArray) {
            if (maxValue < item) {
                maxValue = item;
            }
        }

        // count the number of times each value appears.
        // counts[0] stores the number of 0's in the input
        // counts[4] stores the number of 4's in the input
        // etc.
        int[] counts = new int[maxValue + 1];
        for (int item : theArray) {
            counts[item] += 1;
        }

        // overwrite counts to hold the next index an item with
        // a given value goes. so, counts[4] will now store the index
        // where the next 4 goes, not the number of 4's our
        // array has.
        int numItemsBefore = 0;
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            counts[i] = numItemsBefore;
            numItemsBefore += count;
        }

        // output array to be filled in
        int[] sortedArray = new int[theArray.length];

        // run through the input array
        for (int item : theArray) {

            // place the item in the sorted array
            sortedArray[ counts[item] ] = item;

            // and, make sure the next item we see with the same value
            // goes after the one we just placed
            counts[item] += 1;
        }

        return sortedArray;
    }

}
