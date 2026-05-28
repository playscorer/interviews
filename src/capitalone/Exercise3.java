package capitalone;

import java.util.Arrays;

/**
 * Jeffersonton, VA - May 22th, 2026 - Online Assessment
 *
 * Memory allocation: memory[]
 * Queries: queries[][] = {{0, 1}, {0, 2}, {1, 2}} -> 0= alloc(size=1) ; 1= erase(id=2)
 *
 * I had the good solution but a couple of bugs with indexes, and variables declared at the wrong place
 * Apparently, it is supposed to be the exercise solved last.
 *
 */
public class Exercise3 {

    public static int[] solution(int[] memory, int[][] queries) {
        int idx=0;
        int count=1;
        int[] res = new int[queries.length];
        Arrays.fill(res, -1);

        for (int[] query : queries) {
            int size=0;
            int alloc = query[0];
            int num = query[1];

            System.out.println("query:" + query[0] + " " + query[1]);

            if (alloc == 0) {
                System.out.println("alloc " + num);
                for (int i=0; i<memory.length; i++) {
                    if (memory[i]==0) {
                        size++;
                        if (size == num) {
                            System.out.println("found allocation!");
                            Arrays.fill(memory, i-size+1, i+1, count);
                            count++;
                            res[idx] = i-size+1;
                            break;
                        }
                    } else {
                        size=0;
                    }
                }

            } else {
                System.out.println("erase " + num);
                for (int i=0; i<memory.length; i++) {
                    if (memory[i] == num) {
                        memory[i] = 0;
                        size++;
                    }
                }
                if (size > 0) {
                    res[idx] = size;
                } else {
                    res[idx] = -1;
                }
            }
            idx++;
        }

        return res;
    }

}
