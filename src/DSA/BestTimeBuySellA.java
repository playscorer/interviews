package DSA;

public class BestTimeBuySellA {

    /**
     * Best time to buy and sell stock A
     *
     * You are given an array of prices where prices[i] is the price of a given stock on the ith day.
     *
     * You want to maximize your profit by choosing a single day to buy one stock and choosing a different day
     * in the future to sell that stock.
     *
     * Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
     *
     * Example 1: input prices = [7, 1, 5, 3, 6 ,4]
     * Output: 5
     * Explanation: Buy on day 2 (price=1) and sell on day 5 (price=6), profit = 6-1 = 5.
     * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
     *
     * Example 2: input prices = [7, 6, 4, 3 ,1]
     * Output: 0
     * Explanation: In this case, no transactions are done and the max profit = 0.
     *
     */
    public static int bestTimeBuySellA(int[] prices) {
        int min=Integer.MAX_VALUE;
        int dayMin=0;
        int max=Integer.MIN_VALUE;
        int dayMax=0;

        for (int i=0; i<prices.length; i++) {
            if (min > prices[i]) {
                min = prices[i];
                dayMin = i;
            }
        }

        for (int i=dayMin+1; i<prices.length; i++) {
            if (max < prices[i]) {
                max = prices[i];
                dayMax = i;
            }
        }

        System.out.println("dayMin: " + dayMin + " dayMax: " + dayMax + " min: " + min + " max: " + max);

        if (dayMin < dayMax) {
            return max-min;
        }

        return 0;
    }

    public static int bestTimeBuySellA2(int [] prices) {
        int min=Integer.MAX_VALUE;
        int maxProfit=0;

        for (int i=0; i<prices.length; i++) {
            min = Math.min(min, prices[i]);
            maxProfit = Math.max(maxProfit, prices[i]-min);
        }

        return maxProfit;
    }

    public static void main(String[] args) {
        int[] wrong = {2,4,1};
        System.out.println(bestTimeBuySellA(wrong));
        // returns 0 and should return 2

        int[] ex1 = {7, 1, 5, 3, 6 ,4};
        System.out.println(bestTimeBuySellA2(ex1));

        int[] ex2 = {7, 6, 4, 3 ,1};
        System.out.println(bestTimeBuySellA2(ex2));

        int[] ex3 = {170, 175, 172, 178, 180};
        System.out.println(bestTimeBuySellA2(ex3));
    }

}
