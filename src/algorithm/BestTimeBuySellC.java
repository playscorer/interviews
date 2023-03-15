package algorithm;

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
     *
     * buy1 - the minimum price seen so far for the first transaction
     * profit1 - the maximum profit seen so far for the first transaction
     * buy2 - the minimum price seen so far for the second transaction,
     * taking into account the profit from the first transaction
     * profit2 - the maximum profit seen so far for the second transaction
     *
     * At the end of the iteration, the value of sell2 is returned as the maximum profit achievable
     * with two transactions.
     *
     */
    public static int maxProfit(int[] prices) {
        int profit1=Integer.MIN_VALUE;
        int profit2=Integer.MIN_VALUE;
        int buy1=Integer.MAX_VALUE;
        int buy2=Integer.MAX_VALUE;

        for (int i=0; i<prices.length; i++) {
            buy1 = Math.min(buy1, prices[i]);
            profit1 = Math.max(profit1, prices[i]-buy1);
            buy2 = Math.min(buy2, prices[i]-profit1);
            profit2 = Math.max(profit2, prices[i]-buy2);
            System.out.println("i: " + i + " prices[i]: " + prices[i] + " buy1: " + buy1 + " profit1: " + profit1 + " buy2: " + buy2 + " profit2: " + profit2);
        }

        return profit2;
    }

    /**
     * Take an array and make the best profit by traversing from right to left in one transaction.
     * And then take another array and this time traverse from left to right and take the best profit.
     * And finally, get the anawer by taking the highest value by taking the sum of the two array,
     * one from left and other from right.
     *
     * NOT INTUITIVE AT ALL
     *
     */
    public static int maxProfit2(int[] prices) {
        int min = prices[0];
        int max = prices[prices.length-1];
        int[] ara = new int[prices.length];
        int[] ara2 = new int[prices.length];

        int res=0;
        ara2[0]=0;
        ara[prices.length-1] = 0;

        for(int i=prices.length-2; i>=0; i--){
            ara[i] = Math.max(ara[i+1],max-prices[i]);
            max = Math.max(max, prices[i]);
        }

        Arrays.stream(ara).forEach(x -> System.out.print(x + " "));
        System.out.println();

        for(int i=1; i<prices.length; i++){
            ara2[i] = Math.max(ara2[i-1],prices[i]-min);
            min = Math.min(min, prices[i]);
        }

        Arrays.stream(ara2).forEach(x -> System.out.print(x + " "));
        System.out.print("\nres: ");

        for(int i=0; i<prices.length-1; i++){
            res = Math.max(ara2[i] + ara[i+1], res);
            System.out.print(res + " ");
        }
        System.out.println();

        res = Math.max(res, ara2[prices.length-1]);
        System.out.println("res: " + res);

        res = Math.max(res,ara[0]);
        System.out.println("res: " + res);

        return res;
    }

    public static void main(String[] args) {
        int[] ex = {3,6,0,0,1,7,4,9};
        //System.out.println(maxProfit(ex1));
        System.out.println(maxProfit2(ex));

        int[] ex1 = {3,3,5,0,0,3,1,4};
        //System.out.println(maxProfit(ex1));
        //System.out.println(maxProfit2(ex1));

        int[] ex2 = {1,2,3,4,5};
        //System.out.println(maxProfit(ex2));

        int[] ex3 = {7,6,4,3,1};
        //System.out.println(maxProfit(ex3));
    }

}
