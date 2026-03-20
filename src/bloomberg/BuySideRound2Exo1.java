package bloomberg;

import java.util.ArrayDeque;
import java.util.PriorityQueue;

/*
Imagine you are a Front Office trader, whose job is to execute trades efficiently as instructed by Portfolio Managers.
The largest trades should be executed first. Once in a while, compliance may ask you about the minimal trade you have
that is not yet executed.

Implement a data structure that supports the following operations:

- addTrade(int amount)      : adds a trade
- executeTrade()            : executes a trade and returns the trade amount
- extractMinTrade()         : returns the amount of the minimal trade not yet executed

addTrade(13), addTrade(11), addTrade(9), addTrade(20)
extractMin() -> 9

executeTrade() -> 20
extractMin() -> 9

executeTrade() -> 9
extractMin() -> 11
*/
public class BuySideRound2Exo1 {
    ArrayDeque<Integer> executionQueue = new ArrayDeque<>(); // LIFO
    PriorityQueue<Integer> minInQueue = new PriorityQueue<>();
    Integer minTrade = Integer.MAX_VALUE;

    public void addTrade(int amount) {
        executionQueue.offer(amount);
        minInQueue.offer(amount);
        if (minTrade > amount) { // Max not min
            minTrade = amount;
        }
    }

    public void executeTrade() {
        int trade = executionQueue.pollLast();

        if (trade == minTrade) {
            minInQueue.poll();
            minTrade = minInQueue.peek();
        }
    }

    public int extractMinTrade() {
        return minInQueue.peek();
    }

    // improve complexity
    ArrayDeque<int[]> pairsQueue = new ArrayDeque<>();

    public void addTrade2(int amount) {
        if (minTrade > amount) {
            minTrade = amount;
        }
        pairsQueue.offer(new int[] {amount, minTrade});
    }

    public void executeTrade2() {
        pairsQueue.pollLast();
    }

    public int extractMinTrade2() {
        return pairsQueue.peekLast()[1];
    }

    /**
     * addTrade(13), eQ = 13 | pQ = 13 | min = 13
     * addTrade(11), eQ = 13, 11 | pQ = 11, 13 | min = 11
     * addTrade(9),  eQ = 13, 11, 9 | pQ = 9, 11, 13 | min = 9
     * addTrade(20), eQ = 13, 11, 9, 20 | pQ = 9, 11, 13, 20 | min = 9
     *
     * extractMin() -> 9
     *
     * executeTrade() -> 20 : eQ = 13, 11, 9 | pQ = 9, 11, 13, 20 | min = 9
     * executeTrade() -> 9  : eQ = 13, 11 | pQ = 11, 13, 20 | min = 11
     *
     * PriorityQueue (Heap) push / pop : O (log n) - peek : O (1)
     * the heap property requires sifting an element up/down the height of the tree.
     *
     * Follow up : How to increase time complexity? Remove PriorityQueue and add pairs instead in the Qeueue
     */
    public static void main(String[] args) {
        BuySideRound2Exo1 init = new BuySideRound2Exo1();
        init.addTrade(13);
        init.addTrade(11);
        init.addTrade(9);
        init.addTrade(20);

        init.executionQueue.stream().forEach(a -> System.out.print(a + " "));
        System.out.println("Min trade: " + init.minTrade);
        System.out.println(init.extractMinTrade());

        init.executeTrade();
        System.out.println("Min trade: " + init.minTrade);
        init.executeTrade();
        System.out.println("Min trade: " + init.minTrade);

        System.out.println(init.extractMinTrade());

        // addTrade(13), addTrade(11), addTrade(9), addTrade(20)
        // pairsQueue: 13,13 - 11,11 - 9,9 - 20,9
        System.out.println("Improved solution");
        init = new BuySideRound2Exo1();
        init.addTrade2(13);
        init.addTrade2(11);
        init.addTrade2(9);
        init.addTrade2(20);

        init.pairsQueue.stream().forEach(a -> System.out.print(a[0] + "," + a[1] + " "));
        System.out.println();
        System.out.println(init.extractMinTrade2());

        init.executeTrade2();
        init.executeTrade2();

        System.out.println(init.extractMinTrade2());
    }
}
