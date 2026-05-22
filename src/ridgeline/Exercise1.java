package ridgeline;

import java.util.HashMap;
import java.util.Map;

/**
 *** Exact same interview in 2023 and in May 21st, 2026 (Jeffersonton, VA) ***
 *
 * Step 1 - Accounting (limit 15 minutes):
 * Build a class / object that can track your portfolio.  You do not need to track individual trades or tax lots,
 * but it should capture the net impact of the trade on the portfolio (+/- cash and +/- quantity of shares).
 *
 * It should have methods:
 *     * Buy(ticker: string, quantity: integer, pricePerShare: decimal) : void : exception
 *        - Must have sufficient cash
 *     * Sell(ticker: string, quantity: integer, pricePerShare: decimal) : void : exception
 *        - Must have sufficient quantity
 *     * GetCashBalance() : decimal
 *     * GetQuantityOfShares(ticker: string) : integer
 */
public class Exercise1 {

    static class Accounting {
        private double cashBalance;
        private Map<String, Integer> portfolio;

        public Accounting(double initialCashBalance) {
            if (initialCashBalance > 0) {
                cashBalance = initialCashBalance;
            }
            portfolio = new HashMap<>();
        }

        public void buy(String ticker, Integer qty, double pricePerShare) {
            double buyingAmount = pricePerShare * qty;

            if (cashBalance >= buyingAmount) {
                cashBalance -= buyingAmount;
                portfolio.put(ticker, portfolio.getOrDefault(ticker, 0) + qty);
            }
        }

        public void sell(String ticker, Integer qty, double pricePerShare) {
            int tickerQty = portfolio.getOrDefault(ticker, 0);

            if (tickerQty >= qty) {
                portfolio.put(ticker, tickerQty - qty);
                cashBalance += qty * pricePerShare;
            }
        }

        public double getCashBalance() {
            return cashBalance;
        }

        public int getQuantityOfShares(String ticker) {
            return portfolio.getOrDefault(ticker, 0);
        }
    }

    public static void main(String[] args) {
        // 2023
        Accounting accounting = new Accounting(10000d);
        System.out.println("Initial cashBalance: " + accounting.getCashBalance());

        accounting.buy("AAPL", 50, 150);
        System.out.println("AAPL: " + accounting.getQuantityOfShares("AAPL"));
        System.out.println("cashBalance: " + accounting.getCashBalance());

        accounting.sell("AAPL", 10, 300);
        System.out.println("AAPL: " + accounting.getQuantityOfShares("AAPL"));
        System.out.println("cashBalance: " + accounting.getCashBalance());

        // 2026
        accounting = new Accounting(500d);

        accounting.buy("TSLA", 10, 25d);
        System.out.println(accounting.getCashBalance()); //250
        System.out.println(accounting.getQuantityOfShares("TSLA")); //10

        accounting.sell("TSLA", 5, 30d);
        System.out.println(accounting.getCashBalance()); //400
        System.out.println(accounting.getQuantityOfShares("TSLA")); //5
    }
}