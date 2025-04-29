package ridgeline;

import java.util.Arrays;

/*

    ChatGPT has predicted future prices for stocks in your portfolio.  You decide to build a portfolio
    accounting tool to track your portfolio and generate trades based on the predictions.

    Step 2 - Trading:
    Write code that generates trades to maximize gains in a single stock.  You may trade at most
    once per day, but are not required to trade every day.  Shares must be purchased as whole
    numbers (no fractional shares).  Shares and cash cannot be negative.

    Using code from Step 1 may be helpful, but is not required.

    Starting Cash: 100000
    Ticker: ABCD

    Predicted Stock Price:
    Day 1: 170
    Day 2: 175
    Day 3: 172
    Day 4: 178
    Day 5: 180

    Output:
        * Daily Activity
            Format: (BUY|SELL|HOLD) 100 Shares ABCD at $170 : cash balance $1000
        * BONUS: the ending portfolio value
            Format: Ending Value: $123456 (23.46%)

 */
public class Exercise2 {

    public static enum Order {
        BUY,
        SELL,
        HOLD;
    }

    public static void trade(int[] prices) {
        int [][] dp = new int[prices.length][2];
        Order[] seq = new Order[prices.length];
        System.out.println(f(0, prices, dp, seq, 1));
        Arrays.stream(seq).toList().forEach(System.out::println);
    }

    public static int f(int i, int[] prices, int[][] dp, Order[] seq, int canBuy) {
        if (i == prices.length) {
            return 0;
        }
        if (dp[i][canBuy]>0) {
            return dp[i][canBuy];
        }
        if (canBuy == 1) {
            int buy = -prices[i] + f(i + 1, prices, dp, seq, 0);
            int hold = f(i + 1, prices, dp, seq, 1);

            seq[i] = (buy > hold) ? Order.BUY : Order.HOLD;
            dp[i][canBuy] = Math.max(buy, hold);
        } else {
            int sell = prices[i] + f(i + 1, prices, dp, seq, 1);
            int hold = f(i + 1, prices, dp, seq, 0);

            seq[i] = (sell > hold) ? Order.SELL : Order.HOLD;
            dp[i][canBuy] = Math.max(sell, hold);
        }
        return dp[i][canBuy];
    }

    public static void main(String[] args) {
        int[] ex1 = {170, 175, 172, 178, 180};
        trade(ex1);
    }
}
