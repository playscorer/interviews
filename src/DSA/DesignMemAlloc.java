package DSA;

import java.util.ArrayDeque;
import java.util.Deque;

public class DesignMemAlloc {
    private int[][] memory;
    private int memSize;

    public DesignMemAlloc(int n) {
        memory = new int[n][2];
        this.memSize = n;
        for (int i=0; i<memSize; i++) {
            memory[i][0] = -1;
            memory[i][1] = n-i;
        }
    }

    public int allocate(int size, int mID) {
        int counter=0;
        int idx=-1;

        if (size > memSize) return -1;

        for (int i=0; i<memSize; i++) {
            if (memory[i][0] == -1 & memory[i][1] >= size-counter) {
                memory[i][0] = mID;
                memory[i][1] = size;
                counter++;
            }
            if (counter == 1) idx = i;
            if (counter == size) break;
        }

        return idx;
    }

    public int freeMemory(int mID) {
        Deque<Integer> stack = new ArrayDeque<>();

        int counter = 0;
        for (int i=0; i<memSize; i++) {
            if (memory[i][0] == mID) {
                memory[i][0] = -1;
                memory[i][1] -= counter;
                counter++;
            }
        }

        for (int i=0; i<=memSize; i++) {
            if (i<memSize && memory[i][0] == -1) {
                stack.offer(i);
            } else {
                int cnt =0;
                int avail = stack.size();
                while (!stack.isEmpty()) {
                    memory[stack.poll()][1] = avail - cnt;
                    cnt++;
                }
            }
        }
        return counter;
    }
}
