package DSA;

import static java.lang.Math.max;

public class BestTimeBuySellE {

    /**
     * Best time to buy and sell stock E
     *
     * You are given an array of prices where prices[i] is the price of a given stock on the ith day.
     *
     * Find the maximum profit you can achieve.
     *
     * You may complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:
     * After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
     *
     * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
     *
     * Example 1:
     *
     * Input: prices = [1,2,3,0,2]
     * Output: 3
     * Explanation: transactions = [buy, sell, cooldown, buy, sell]
     *
     * Example 2:
     *
     * Input: prices = [1]
     * Output: 0
     *
     */

    public static int bestTimeBuySellE(int[] prices) {
        int [][] dp = new int[prices.length][2];
        return f(0, prices, dp, 1);
    }

    public static int f(int i, int[] prices, int[][] dp, int canBuy) {
        if (i >= prices.length) {
            return 0;
        }
        if (dp[i][canBuy]>0) {
            return dp[i][canBuy];
        }
        if (canBuy == 1) {
            dp[i][canBuy] = max(-prices[i]+f(i+1, prices, dp, 0), f(i+1, prices, dp, 1));
        } else {
            dp[i][canBuy] = max(prices[i]+f(i+2, prices, dp, 1), f(i+1, prices, dp, 0));
        }
        return dp[i][canBuy];
    }

    /**
     * Using a state machine.
     * s0 - cooldown
     * s1 - bought stock
     * s2 - sold stock
     */
    public static int bestTimeBuySellE2(int[] prices) {
        int[] s0 = new int[prices.length];
        int[] s1 = new int[prices.length];
        int[] s2 = new int[prices.length];

        s0[0] = 0;
        s1[0] = -prices[0];
        s2[0] = 0;

        for (int i = 1; i < prices.length; i++) {
            s0[i] = max(s0[i - 1], s2[i - 1]);
            s1[i] = max(s1[i - 1], s0[i - 1] - prices[i]);
            s2[i] = s1[i - 1] + prices[i];
        }
        return max(s0[prices.length - 1], s2[prices.length - 1]);
    }

    public static void main(String[] args) {
        int[] ex1 = {7, 1, 5, 3, 6 ,4};
        //System.out.println(bestTimeBuySellE(ex1));
        System.out.println(bestTimeBuySellE2(ex1));

        int[] ex2 = {7, 6, 4, 3 ,1};
        //System.out.println(bestTimeBuySellE(ex2));
        System.out.println(bestTimeBuySellE2(ex2));
    }

}
