package bloomberg;

import java.util.*;

/**
 *
 * 2/9/2026 - Interview Round 1 for Trade Execution
 *
 * Time Complexity is O (fromCurrencies + toCurrencies + fromCurrencies) = O (V + E)
 * It means at most all vertices (or nodes/currencies) and all edges (or rates) are visited.
 * I got confused with DFS + Backtracking
 *   -> You are not choosing how to visit a node — only whether a path exists (Path enumeration Vs. Reachability).
 *
 * “If I make a wrong choice, do I need to undo it to try another?”
 *  Currency conversion → No
 *  Word Search → Yes
 */
public class CurrencyConverter {

    private Map<String, Map<String, Double>> graphMap;

    public CurrencyConverter(List<String> fromCurrencies,
                             List<String> toCurrencies,
                             List<Double> rates) {

        graphMap = new HashMap<>();

        for (int i = 0; i < fromCurrencies.size(); i++) {
            // rates
            graphMap.computeIfAbsent(fromCurrencies.get(i), k -> new HashMap<>())
                    .put(toCurrencies.get(i), rates.get(i));

            // reverse rates to complete with all pairs
            graphMap.computeIfAbsent(toCurrencies.get(i), k -> new HashMap<>())
                    .put(fromCurrencies.get(i), 1 / rates.get(i));
        }
    }

    // Returns the multiplicative conversion ratio.
    // Returns -1.0 if conversion is impossible / unknown currencies.
    public double getConversionRatio(String fromCurrency, String toCurrency) {
        return dfs(fromCurrency, toCurrency, new HashSet<>());
    }

    public double dfs(String fromCurrency, String toCurrency, Set<String> visited) {
        System.out.println("getConversionRatio: " + fromCurrency + " -> " + toCurrency);

        if (fromCurrency.equals(toCurrency)) return 1.0;

        if (graphMap.containsKey(fromCurrency)) {
            visited.add(fromCurrency);
            Map<String, Double> toCurrenciesMap = graphMap.get(fromCurrency);
            if (toCurrenciesMap.containsKey(toCurrency)) {
                return toCurrenciesMap.get(toCurrency);
            } else {
                for (Map.Entry<String, Double> pairCurrency : toCurrenciesMap.entrySet()) {
                    String interCurrency = pairCurrency.getKey();
                    if (!visited.contains(interCurrency)) {
                        System.out.println("getConversionRatio: " + fromCurrency + " -> " + interCurrency);
                        double rate =
                                pairCurrency.getValue() * dfs(pairCurrency.getKey(), toCurrency, visited);
                        if (rate != -1) {
                            return rate;
                        }
                    }
                }
                // backtrack
            }
        }
        return -1.0;
    }

    public static void main(String[] args) {

        List<String> fromCurrencies = Arrays.asList(
                "EUR", "USD", "GBP", "AUD", "USD",
                "USD", "EUR", "EUR", "USD", "EUR",
                "USD"
        );

        List<String> toCurrencies = Arrays.asList(
                "USD", "JPY", "USD", "USD", "CAD",
                "CHF", "JPY", "GBP", "HKD", "CHF",
                "KRW"
        );

        List<Double> rates = Arrays.asList(
                1.0006,   // EUR -> USD
                144.3900, // USD -> JPY
                1.1529,   // GBP -> USD
                0.6764,   // AUD -> USD
                1.3125,   // USD -> CAD
                0.9606,   // USD -> CHF
                144.4600, // EUR -> JPY
                0.8679,   // EUR -> GBP
                7.8494,   // USD -> HKD
                0.9611,   // EUR -> CHF
                1374.09   // USD -> KRW
        );

        // CHF -> JPY
        CurrencyConverter ccyConverter =
                new CurrencyConverter(fromCurrencies, toCurrencies, rates);

        System.out.println(ccyConverter.getConversionRatio("CHF", "JPY"));
    }
}
