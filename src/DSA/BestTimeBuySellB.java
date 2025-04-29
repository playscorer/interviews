package DSA;

public class BestTimeBuySellB {

    /**
     * Best time to buy and sell stock B
     *
     * You are given an array of prices where prices[i] is the price of a given stock on the ith day.
     *
     * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the
     * stock at any time. However, you can buy it then immediately sell it on the same day.
     *
     * Find and return the maximum profit you can achieve.
     *
     * Example 1: input prices = [7, 1, 5, 3, 6 ,4]
     * Output: 7
     * Explanation: Buy on day 2 (price=1) and sell on day 3 (price=5), profit = 5-1 = 4.
     * Then buy on day 4 (price=3) and sell on day 5 (price=6), profit = 6-3 =3.
     * Total profit is 4 + 3 = 7.
     *
     * Example 2: input prices = [1, 2, 3, 4, 5]
     * Output: 4
     * Explanation: Buy on day 1 (price=1) and sell on day 5 (price=5), profit = 5-1 = 4.
     * Total profit is 4.
     *
     * Example 3: input prices = [7, 6, 4, 3, 1]
     * Output: 0
     * Explanation: There is no way to make a positive profit, so we never buy the stock to achieve the maximum profit of 0.
     *
     */
    public static int bestTimeBuySellB(int[] prices) {
        int length = prices.length;
        int profit=0;

        for (int i=0; i<length-1; i++) {
            //if (prices[i] < prices[i+1]) {
                profit += Math.max(0, prices[i+1]-prices[i]);
            //}
        }

        return profit;
    }

    public static int bestTimeBuySellB2(int[] prices) {
        int [][] dp = new int[prices.length][2];
        return f(0, prices, dp, 1);
    }

    public static int f(int i, int[] prices, int[][] dp, int canBuy) {
        if (i == prices.length) {
            return 0;
        }
        if (dp[i][canBuy]>0) {
            return dp[i][canBuy];
        }
        if (canBuy == 1) {
            dp[i][canBuy] = Math.max(-prices[i]+f(i+1, prices, dp, 0), f(i+1, prices, dp, 1));
        } else {
            dp[i][canBuy] = Math.max(prices[i]+f(i+1, prices, dp, 1), f(i+1, prices, dp, 0));
        }
        return dp[i][canBuy];
    }

    public static void main(String[] args) {
        int[] ex1 = {7, 1, 5, 3, 6 ,4};
        //System.out.println(bestTimeBuySellB(ex1));
        System.out.println(bestTimeBuySellB2(ex1));

        int[] ex2 = {7, 6, 4, 3 ,1};
        //System.out.println(bestTimeBuySellB(ex2));
        System.out.println(bestTimeBuySellB2(ex2));
    }

}
