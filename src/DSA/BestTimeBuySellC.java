package DSA;

import java.util.Arrays;

public class BestTimeBuySellC {

    /**
     * Best time to buy and sell stock C
     *
     * You are given an array of prices where prices[i] is the price of a given stock on the ith day.
     *
     * Find the maximum profit you can achieve. You may complete at most two transactions.
     *
     * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
     *
     * Example 1: Input: prices = [3,3,5,0,0,3,1,4]
     * Output: 6
     * Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
     * Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
     *
     * Example 2: Input: prices = [1,2,3,4,5]
     * Output: 4
     * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
     * Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are engaging multiple transactions at the same time. You must sell before buying again.
     *
     * Example 3: Input: prices = [7,6,4,3,1]
     * Output: 0
     * Explanation: In this case, no transaction is done, i.e. max profit = 0.
     *
     */

    /**
     * The basic idea is to iterate over the array of stock prices and update four variables:
     * <p>
     * buy1 - the minimum price seen so far for the first transaction
     * profit1 - the maximum profit seen so far for the first transaction
     * buy2 - the minimum price seen so far for the second transaction,
     * taking into account the profit from the first transaction
     * profit2 - the maximum profit seen so far for the second transaction
     * <p>
     * At the end of the iteration, the value of sell2 is returned as the maximum profit achievable
     * with two transactions.
     */
    public static int maxProfit(int[] prices) {
        int buy1 = Integer.MAX_VALUE;
        int sell1 = Integer.MIN_VALUE;
        int buy2 = Integer.MAX_VALUE;
        int sell2 = Integer.MIN_VALUE;

        for (int i = 0; i < prices.length; i++) {
            // update first buy and sell values
            buy1 = Math.min(buy1, prices[i]);
            sell1 = Math.max(sell1, prices[i] - buy1);
            // update second buy and sell values
            buy2 = Math.min(buy2, prices[i] - sell1);
            sell2 = Math.max(sell2, prices[i] - buy2);
            System.out.println("i: " + i + " prices[i]: " + prices[i] + " buy1: " + buy1 + " sell1: " + sell1 + " buy2: " + buy2 + " sell2: " + sell2);
        }

        return sell2;
    }

    /**
     * The idea is to create two arrays say leftMaxProfit and rightMaxProfit to store the profit of the subarrays.
     * <p>
     * Traverse the given array from start to end and for each i update leftMaxProfit[i] to maximum profit that can be obtained from 0 to i by doing at most one transaction.
     * Traverse the given array from end to start and for each index i update rightMaxProfit[i] to maximum profit that can be obtained from i to n by doing at most one transaction.
     * Now traverse both the arrays and update the answer as max(answer, leftMaxProfit[i] + rightMaxProfit[i]) for every i.
     * <p>
     * NOT INTUITIVE AT ALL
     */
    public static int maxProfit2(int[] prices) {
        int min = prices[0];
        int max = prices[prices.length - 1];
        int[] leftMaxProfit = new int[prices.length];
        int[] rightMaxProfit = new int[prices.length];

        int finalResult = 0;
        leftMaxProfit[0] = 0;
        rightMaxProfit[prices.length - 1] = 0;

        for (int i = 1; i < prices.length; i++) {
            leftMaxProfit[i] = Math.max(leftMaxProfit[i - 1], prices[i] - min);
            min = Math.min(min, prices[i]);
        }

        System.out.print("\nleftMaxProfit: ");
        Arrays.stream(leftMaxProfit).forEach(x -> System.out.print(x + " "));

        for (int i = prices.length - 2; i >= 0; i--) {
            rightMaxProfit[i] = Math.max(rightMaxProfit[i + 1], max - prices[i]);
            max = Math.max(max, prices[i]);
        }

        System.out.print("\nrightMaxProfit: ");
        Arrays.stream(rightMaxProfit).forEach(x -> System.out.print(x + " "));
        System.out.println();

        System.out.print("\nfinalResult: ");

        for (int i = 0; i < prices.length - 1; i++) {
            finalResult = Math.max(leftMaxProfit[i] + rightMaxProfit[i + 1], finalResult);
            System.out.print(finalResult + " ");
        }
        System.out.println();

        finalResult = Math.max(finalResult, leftMaxProfit[prices.length - 1]);
        System.out.println("answer: " + finalResult);

        finalResult = Math.max(finalResult, rightMaxProfit[0]);
        System.out.println("answer: " + finalResult);

        return finalResult;
    }

    public static int maxProfit3(int[] prices) {
        /*
        for (int i = 0; i < prices.length; i++) {
            // update first buy and sell values
            buy1 = Math.min(buy1, prices[i]);
            profit1 = Math.max(profit1, prices[i] - buy1);
            // update second buy and sell values
            buy2 = Math.min(buy2, prices[i] - profit1);
            profit2 = Math.max(profit2, prices[i] - buy2);
        }
         */
        int[] leftMaxProfit = new int[prices.length];
        int[] rightMaxProfit = new int[prices.length];
        int buy1 = Integer.MAX_VALUE;
        int buy2 = Integer.MAX_VALUE;

        int finalResult = 0;

        for (int i = 0; i < prices.length; i++) {
            // update first buy and sell values
            buy1 = Math.min(buy1, prices[i]);
            System.out.print(" " + (prices[i] - buy1) + "," + leftMaxProfit[i]);
            leftMaxProfit[i] = Math.max(leftMaxProfit[i], prices[i] - buy1);
            System.out.print(" " + leftMaxProfit[i]);
        }

        System.out.println();

        for (int i = prices.length-1; i >= 0; i--) {
            // update second buy and sell values
            buy2 = Math.min(buy2, prices[i]);
            System.out.print(" " + (prices[i] - buy2) + "," + rightMaxProfit[i]);
            rightMaxProfit[i] = Math.max(rightMaxProfit[i], prices[i] - buy2);
            System.out.print(" " + rightMaxProfit[i]);
        }

        System.out.println();

        for (int i = 0; i < prices.length - 1; i++) {
            finalResult = Math.max(leftMaxProfit[i] + rightMaxProfit[i + 1], finalResult);
            System.out.print(finalResult + " ");
        }
        System.out.println();

        finalResult = Math.max(finalResult, leftMaxProfit[prices.length - 1]);
        System.out.println("answer: " + finalResult);

        finalResult = Math.max(finalResult, rightMaxProfit[0]);
        System.out.println("answer: " + finalResult);

        return finalResult;
    }

    public static void main(String[] args) {
        int[] ex = {3, 6, 0, 0, 1, 7, 4, 9};
        //System.out.println(maxProfit(ex1));
        //System.out.println(maxProfit2(ex));
        System.out.println(maxProfit3(ex));

        int[] ex1 = {3, 3, 5, 0, 0, 3, 1, 4};
        //System.out.println(maxProfit(ex1));
        //System.out.println(maxProfit2(ex1));

        int[] ex2 = {1, 2, 3, 4, 5};
        //System.out.println(maxProfit(ex2));

        int[] ex3 = {7, 6, 4, 3, 1};
        //System.out.println(maxProfit(ex3));
    }

}
