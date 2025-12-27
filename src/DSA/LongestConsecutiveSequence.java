package DSA;

import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class LongestConsecutiveSequence {

    public static int longestConsecutive(int[] nums) {
        var pq = new PriorityQueue<Integer>();
        int cpt=0;
        int max=-1;
        int prev=-1;
        int idx=0;

        IntStream.of(nums).forEach(i -> pq.offer(i));
        while (!pq.isEmpty()) {
            int i = pq.poll();

            if (idx>0 && prev == i) {
                continue;
            }

            if (idx==0 || prev + 1 == i) {
                cpt++;
            } else {
                if (max == -1 || max <= cpt) {
                    max = cpt;
                }
                cpt=1;
            }

            prev=i;
            idx++;
        }

        if (max < cpt) {
            max = cpt;
        }

        return max;
    }

    public static void main(String[] args) {
        int res = LongestConsecutiveSequence.longestConsecutive(new int[] {100,4,200,1,3,2});
        System.out.println(res);
    }
}
