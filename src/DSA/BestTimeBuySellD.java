package DSA;

public class BestTimeBuySellD {

    /**
     * Best time to buy and sell stock D
     *
     * You are given an integer array prices where prices[i] is the price of a given stock on the ith day,
     * and an integer k.
     *
     * Find the maximum profit you can achieve. You may complete at most k transactions:
     * i.e. you may buy at most k times and sell at most k times.
     *
     * Note: You may not engage in multiple transactions simultaneously
     * (i.e., you must sell the stock before you buy again).
     *
     * Example 1:
     *
     * Input: k = 2, prices = [2,4,1]
     * Output: 2
     * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
     *
     * Example 2:
     *
     * Input: k = 2, prices = [3,2,6,5,0,3]
     * Output: 7
     * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
     * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
     *
     */

    public static int maxProfit(int k, int[] prices) {
        int[] buy = new int[k];
        int[] sell = new int[k];

        for (int i=0; i<k; i++) {
            buy[i] = Integer.MAX_VALUE;
            sell[i] = 0;
        }

        for (int p : prices) {
            buy[0] = Math.min(buy[0], p);
            sell[0] = Math.max(sell[0], p - buy[0]);
            for (int i=1; i<k; i++) {
                buy[i] = Math.min(buy[i], p - sell[i-1]);
                sell[i] = Math.max(sell[i], p - buy[i]);
            }
        }

        return sell[k-1];
    }

    public static void main(String[] args) {
        int[] ex = {2, 4, 1};
        System.out.println(maxProfit(2, ex));

        int[] ex1 = {3, 2, 6, 5, 0, 3};
        System.out.println(maxProfit(2, ex1));
    }
}
