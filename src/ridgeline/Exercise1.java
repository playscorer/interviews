package ridgeline;

/*

    ChatGPT has predicted future prices for stocks in your portfolio.  You decide to build a portfolio
    accounting tool to track your portfolio and generate trades based on the predictions.

    Step 1 - Accounting:
    Build a class / object that can track your portfolio.  You don’t need to track individual trades, but
    it should capture the net impact of the trade on the portfolio (+/- cash and +/- quantity of shares).

    It should have methods:
        * Buy(ticker, quantity, pricePerShare) : void : exception
           - Must have sufficient cash
        * Sell(ticker, quantity, pricePerShare) : void : exception
           - Must have sufficient quantity
        * GetCashBalance() : decimal
        * GetQuantityOfShares(ticker) : integer

 */

import java.util.HashMap;
import java.util.Map;

public class Exercise1 {
    public static void main(String[] args) {
        Accounting acc = new Accounting(10000d);
        System.out.println("Initial cashBalance: " + acc.getCashBalance());

        acc.buy("AAPL", 50, 150);
        System.out.println("AAPL: " + acc.getQuantityOfShares("AAPL"));
        System.out.println("cashBalance: " + acc.getCashBalance());

        acc.sell("AAPL", 10, 300);
        System.out.println("AAPL: " + acc.getQuantityOfShares("AAPL"));
        System.out.println("cashBalance: " + acc.getCashBalance());
    }
}

class Accounting {

    private double cashBalance;

    private Map<String, Integer> portfolio;

    public Accounting(double initialCashBalance) {
        cashBalance = initialCashBalance;
        portfolio = new HashMap<>();
    }

    public void buy(String ticker, Integer qty, double pricePerShare) {
        double buyingPrice = pricePerShare * qty;

        if (cashBalance >= buyingPrice) {
            cashBalance -= buyingPrice;
        }

        Integer qtyTicker = portfolio.get(ticker);
        if (qtyTicker == null) {
            portfolio.put(ticker, qty);
        } else {
            portfolio.put(ticker, qtyTicker+qty);
        }
    }

    public void sell(String ticker, Integer qty, double pricePerShare) {
        int tickerQty = portfolio.get(ticker);

        if (tickerQty >= qty) {
            portfolio.put(ticker, tickerQty - qty);
        }

        cashBalance += qty * pricePerShare;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Integer getQuantityOfShares(String ticker) {
        return portfolio.get(ticker);
    }

}