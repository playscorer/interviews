package DSA;

import java.util.Arrays;

public class DesignMemAllocSimple {
    int[] memory;

    public DesignMemAllocSimple(int n) {
        memory = new int[n];
    }

    public int allocate(int size, int mID) {
        if (size > memory.length) return -1;

        int count = 0;
        int startIndex = 0;

        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == 0) {
                count++;
            } else {
                count = 0;
                startIndex = i + 1;
            }
            if (count == size) {
                Arrays.fill(memory, startIndex, startIndex + size, mID);
                return startIndex;
            }
        }

        return -1;
    }

    public int freeMemory(int mID) {
        int count = 0;
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == mID) {
                count++;
                memory[i] = 0;
            }
        }
        return count;
    }
}
