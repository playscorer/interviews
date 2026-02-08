package DSA;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Feb 3rd - Rio de Janeiro
 * I found the solution very quickly but I realized I was using the wrong peek and pop methods
 * with FIFO behaviour instead of LIFO
 */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        Deque<int[]> stack = new ArrayDeque<>();

        for (int i=0; i<intervals.length; i++) {
            //if (!stack.isEmpty())
            //  System.out.println(stack.peekLast()[1] + ">=?" + intervals[i][0]);
            if (!stack.isEmpty() && stack.peekLast()[1] >= intervals[i][0]) {
                int[] interval = stack.pollLast();
                stack.offer(new int[] {interval[0], Math.max(interval[1], intervals[i][1])});
            } else {
                stack.offer(intervals[i]);
            }
            //stack.stream().forEach(it -> System.out.print("[" + it[0] + " " + it[1] + "] "));
            System.out.println();
        }

        return stack.stream().toArray(int[][]::new);
    }
}
