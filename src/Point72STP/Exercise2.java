package Point72STP;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Server Memory Allocation - Problem quite similar to Leetcode 455 - Assign Cookies
 *
 * Given n programs and n - 1 servers, each program must run on its own server,
 * and each server can handle only one program. Each program has a memory
 * requirement provided in the memoryRequirement array, and each existing
 * server has a fixed memory capacity provided in the memoryAvailable array.
 *
 * Since there is one more program than servers, a new server must be added.
 * This method determines the smallest memory capacity required for the new
 * server so that all programs can be executed.
 */

public class Exercise2 {
    /**
     * @param memoryRequirement an array of memory requirements for the programs
     * @param memoryAvailable  an array of memory capacities of the existing servers
     *                         (size is memoryRequirement.length - 1)
     * @return the minimum memory capacity required for the new server, or -1 if
     *         it is impossible to allocate all programs to servers
     */
    public static int getMinMemory(List<Integer> memoryRequirement, List<Integer> memoryAvailable) {
        Collections.sort(memoryRequirement, Comparator.reverseOrder());
        Collections.sort(memoryAvailable, Comparator.reverseOrder());

        int i=0, j=0;
        while (i<memoryRequirement.size() && j<memoryAvailable.size()) {
            if (memoryAvailable.get(j) >= memoryRequirement.get(i)) {
                i++;
                j++;
            } else {
                i++;
            }
        }
        if (i == j) {
            return memoryRequirement.get(i + 1);
        }

        return -1;
    }
}
