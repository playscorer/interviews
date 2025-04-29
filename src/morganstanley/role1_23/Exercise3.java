package morganstanley.role1_23;

public class Exercise3 {

    /**
     * Best time to buy and sell stock B
     */

    public static int bestTimeBuySellB(int [] prices) {
        return f(0, prices, true);
    }

    public static int f(int i, int[] prices, boolean canBuy) {
        if (i == prices.length) {
            return 0;
        }
        if (canBuy) {
            return Math.max(-prices[i] + f(i+1, prices, false),
                    0 + f(i+1, prices, true));
        } else {
            return Math.max(prices[i] + f(i+1, prices, true),
                    0 + f(i+1, prices, false));
        }
    }

    public static int bestTimeBuySellB2(int[] prices) {
        int length = prices.length;
        int profit=0;

        for (int i=0; i<length-1; i++) {
            profit += Math.max(0, prices[i+1]-prices[i]);
        }

        return profit;
    }

    public static void main(String[] args) {
        int[] ex1 = {7, 1, 5, 3, 6, 4};
        //System.out.println(bestTimeBuySellB(ex1));
        System.out.println(bestTimeBuySellB(ex1));
    }
}
