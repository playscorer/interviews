package ridgeline;

/**
 *** Exact same interview in 2023 and in May 21st, 2026 (Jeffersonton, VA) ***
 *
 * Step 2 - Trading (limit 30 minute):
 * Given a list of predicted daily prices for a stock, write code that generates trades to maximize gains in that stock.
 *
 * Instructions:
 * You may trade at most once per day, but are not required to trade every day.
 * Shares must be purchased as whole numbers (no fractional shares).
 * Shares and cash cannot be negative.
 * The position should be liquidated when there is no remaining price data.
 * Log what actions are taken each day (see “Output” below).
 * There are multiple ways to solve this problem!
 *
 * Using code from Step 1 may be helpful, but is not required.
 *
 * Starting Cash: 100_000
 * Ticker: ABCD
 *
 * Predicted Daily Prices for ABCD:
 *  [
 *   170, // Day 1
 *   175, // Day 2
 *   172, // Day 3
 *   178, // Day 4
 *   180 // Day 5
 *  ]
 *
 * Output:
 *     * Daily Activity
 *         Format: (BUY|SELL|HOLD) 100 Shares ABCD at $170 : cash balance $1000
 *     * BONUS: the ending portfolio value
 *         Format: Ending Value: $123456 (23.46%)
 */
public class Exercise2 {

    /**
     * The pattern here is a classic greedy with state.
     * Buy at local minimum, hold until the peak before a drop or end of data, then sell.
     *
     * For [170, 175, 172, 178, 180]:
     *
     * Buy day 1 at 170
     * Day 2 is 175 — higher, but day 3 drops to 172, so sell day 2 at 175
     * Buy day 3 at 172
     * Day 4 is 178, day 5 is 180 — keep rising, sell day 5 at 180
     *
     * - if not holding AND prices[i] < prices[i+1] → BUY
     * - if holding AND (prices[i] > prices[i+1] OR last day) → SELL
     * - else → HOLD
     *
     */
    public static int maxProfit(int initialCash, int[] prices) {
        StringBuilder sb = new StringBuilder();
        int cash=initialCash;
        boolean hold = false;
        int numberOfShares = 0;

        for (int i=0; i<prices.length; i++) {
            // CAN BUY
            if (!hold && i<prices.length-1 && prices[i] < prices[i+1]) {
                numberOfShares = cash / prices[i];
                cash -= numberOfShares * prices[i];
                sb.append("BUY " + numberOfShares + " Shares ABCD at $" + prices[i] + " : cash balance $" + cash + "\n");
                hold = true;
            }
            // CAN SELL
            else if (hold && ((i < prices.length-1 && prices[i] > prices[i+1]) || i == prices.length-1)) {
                cash += numberOfShares * prices[i];
                sb.append("SELL " + numberOfShares + " Shares ABCD at $" + prices[i] + " : cash balance $" + cash + "\n");
                hold = false;
                numberOfShares = 0;
            }
            // HOLD
            // holding and price still rising, or not holding and price falling, or not holding, price raising on last day
            else {
                sb.append("HOLD " + numberOfShares + " Shares ABCD at $" + prices[i] + " : cash balance $" + cash + "\n");
            }
        }

        System.out.println(sb);
        System.out.println("$" + cash + " (%.1f%%)".formatted((double)(cash-initialCash)/initialCash*100));

        return cash;
    }

    /**
     * Fixed Solution in 2026 and removed the DP version that was buggy.
     */
    public static void main(String[] args) {
        int[] prices = {170, 175, 172, 178, 180};
        maxProfit(100000, prices);

        System.out.println();
        prices = new int[] {170, 172, 175};
        maxProfit(100000, prices);
    }
}
