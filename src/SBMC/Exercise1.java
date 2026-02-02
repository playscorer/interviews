package SBMC;

import java.util.*;

/**
 * Process Scheduler - similar to LeetCode 253 — Meeting Rooms II
 *
 * Process scheduling algorithms are used by a CPU
 * to optimally schedule the running processes.
 * A core can execute one process at a time, but a CPU
 * may have multiple cores.
 *
 * There are n processes where the i-th process starts
 * its execution at start[i] and ends at end[i], both
 * inclusive. Find the minimum number of cores required
 * to execute the processes.
 *
 * Example
 *
 * n=3
 * start=[1,3,4]
 * end=[3,5,6]
 * Output: 2
 *
 * If the CPU has only one core, the first process starts
 * at 1 and ends at 3. The second process starts at 3.
 * Since both processes need a processor at 3, they overlap.
 * There must be more than 1 core.
 *
 */
public class Exercise1 {

    /**
     * Solution with PriorityQueues not properly working.
     */
    public static int findMinCores(List<Integer> start, List<Integer> end) {
        int nbCores = 1;
        PriorityQueue<Integer> pMin = new PriorityQueue<>();
        PriorityQueue<Integer> pMax = new PriorityQueue<>(Comparator.reverseOrder());

        for (int i=0; i<start.size(); i++) {
            int pStart = start.get(i);
            int pEnd = end.get(i);

            if (!pMin.isEmpty() && pMin.peek() >= pStart
                && !pMax.isEmpty() && pMax.peek() <= pStart) {
                nbCores++;
            }

            pMin.offer(pEnd);
            pMax.offer(pStart);
        }

        return nbCores;
    }

    public static int findMinCores2(List<Integer> start, List<Integer> end) {
        int nbCores;
        // Array to store the number of
        // lectures ongoing at time t
        int[] prefix_sum = new int[10000];

        // For every lecture increment start
        // point s decrement (end point + 1)
        for (int i = 0; i < start.size(); i++) {
            prefix_sum[start.get(i)]++;
            prefix_sum[end.get(i) + 1]--;
        }

        nbCores = prefix_sum[0];

        // Perform prefix sum and update
        // the ans to maximum
        for (int i = 1; i < prefix_sum.length; i++) {
            prefix_sum[i] += prefix_sum[i - 1];
            nbCores = Math.max(nbCores, prefix_sum[i]);
        }
        return nbCores;
    }

    public static void main(String[] args) {
        Integer[] start={1,3,4};
        Integer[] end={3,5,6};
        System.out.println(findMinCores(Arrays.asList(start), Arrays.asList(end)));
        System.out.println(findMinCores2(Arrays.asList(start), Arrays.asList(end)));

        start= new Integer[]{0,1,1};
        end= new Integer[]{5,2,10};
        System.out.println(findMinCores(Arrays.asList(start), Arrays.asList(end)));
        System.out.println(findMinCores2(Arrays.asList(start), Arrays.asList(end)));

        start= new Integer[]{0,1,6};
        end= new Integer[]{5,2,10};
        System.out.println(findMinCores(Arrays.asList(start), Arrays.asList(end)));
        System.out.println(findMinCores2(Arrays.asList(start), Arrays.asList(end)));

        start= new Integer[]{0,3,4,5};
        end= new Integer[]{3,6,6,7};
        System.out.println(findMinCores(Arrays.asList(start), Arrays.asList(end)));
        System.out.println(findMinCores2(Arrays.asList(start), Arrays.asList(end)));
    }

}
