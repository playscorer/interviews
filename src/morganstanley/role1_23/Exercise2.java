package morganstanley.role1_23;

public class Exercise2 {

    /**
     * Best time to buy and sell stock A
     *
     * "If you are selling on the i-th day, you buy on the minimum price from 1st day to (i-1)-th day"
     */
    public static int bestTimeBuySellA(int [] prices) {
        int min = prices[0];
        int profit = 0;
        for (int i=1; i<prices.length; i++) {
            profit = Math.max(profit, prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        return profit;
    }

    public static void main(String[] args) {
        int[] ex1 = {7, 1, 5, 3, 6, 4};
        System.out.println(bestTimeBuySellA(ex1));
    }
}
