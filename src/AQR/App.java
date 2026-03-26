package AQR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AQR - Interview 4 (Round 2) on 03/20/2026 - NYC
 */
public class App {

    // Data model matching incoming JSON keys
    public static class Transaction {
        public String transactionId;
        public String userId;
        public double amount;
        public long timestamp;

        public Transaction() {}

        @Override
        public int hashCode() {
            return transactionId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Transaction transaction = (Transaction) obj;
            return this.transactionId.equals(transaction.transactionId);
        }
    }

    public static class UserMetrics {
        // Implement here
        double totalSpend;
        int transactionCount;
        double avgAmount;

        public UserMetrics(double totalSpend, int transactionCount, double avgAmount) {
            this.totalSpend = totalSpend;
            this.transactionCount = transactionCount;
            this.avgAmount = avgAmount;
        }

        /**
         * Getters needed to produce JSON output (I failed to add them during the interview...)
         */
        public double getTotalSpend() {
            return totalSpend;
        }

        public int getTransactionCount() {
            return transactionCount;
        }

        public double getAvgAmount() {
            return avgAmount;
        }
    }

    /*
     * Returns Map<user_id, UserMetrics>. If same transaction id repeats, ignore the previous
     * latest one.
     *
     * "adam": {"totalSpend": ..., "transactionCount": ..., "averageTransaction": ...}
     * "april": {"totalSpend": ..., "transactionCount": ..., "averageTransaction": ...}
     */
    public static Map<String, UserMetrics> computeUserMetrics(List<Transaction> transactions) {
        Map<String, UserMetrics> resultMap = new HashMap<>();
        /*
         * During the interview, I provided a Set instead of an internal Map.
         * It provided the first and not last duplicated transaction.
         *
         * Both Set and Map reject duplicate keys. The real difference is:
         *     Set has no value slot — there's nothing to overwrite, so a collision just leaves the original element untouched
         *     Map has a value slot — a collision replaces the value, but the key object itself also gets left as the original
         *
         * Time Complexity: O(n)
         * Space Complexity: O(n) since u ≤ n
         *     tranMap — holds all unique transactions grouped by userId → O(n)
         *     resultMap — one UserMetrics entry per unique user → O(u) where u = number of unique users
         *
         * n = total transactions = u × t
         *     u = number of unique users
         *     t = average transactions per user
         */
        Map<String, Map<String, Transaction>> tranMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            tranMap.computeIfAbsent(transaction.userId, k -> new HashMap<>()).put(transaction.transactionId, transaction); // putIfAbsent keeps the first duplicate
        }

        for (Map.Entry<String, Map<String, Transaction>> entry : tranMap.entrySet()) {
            Collection<Transaction> transByUser = entry.getValue().values();

            double sum = transByUser.stream().mapToDouble(t -> t.amount).sum();
            int count = transByUser.size();
            double avg = count == 0 ? 0 : sum / count;

            resultMap.put(entry.getKey(), new UserMetrics(sum, count, avg));
        }

        return resultMap;
    }

    public static Map<String, UserMetrics> computeUserMetricsStream(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.userId,
                        Collectors.collectingAndThen(
                                Collectors.toMap(
                                        t -> t.transactionId,
                                        t -> t,
                                        (existing, replacement) -> replacement  // last-wins on duplicate transactionId
                                ),
                                tranMap -> {
                                    Collection<Transaction> deduped = tranMap.values();
                                    double sum = deduped.stream().mapToDouble(t -> t.amount).sum();
                                    int count = deduped.size();
                                    double avg = count == 0 ? 0 : sum / count;
                                    return new UserMetrics(sum, count, avg);
                                }
                        )
                ));
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String input = "["
                + "{\"transactionId\":\"tx001\",\"userId\":\"adam\",\"amount\":120.5,\"timestamp\":1700000000},"
                + "{\"transactionId\":\"tx002\",\"userId\":\"jack\",\"amount\":75.0,\"timestamp\":1700000100},"
                + "{\"transactionId\":\"tx003\",\"userId\":\"adam\",\"amount\":45.25,\"timestamp\":1700000600},"
                + "{\"transactionId\":\"tx004\",\"userId\":\"adam\",\"amount\":200.0,\"timestamp\":1700000900},"
                + "{\"transactionId\":\"tx005\",\"userId\":\"jack\",\"amount\":-15.75,\"timestamp\":1700001200},"
                + "{\"transactionId\":\"tx006\",\"userId\":\"jack\",\"amount\":500.0,\"timestamp\":1700001500},"
                + "{\"transactionId\":\"tx001\",\"userId\":\"adam\",\"amount\":30.0,\"timestamp\":1700001800},"
                + "{\"transactionId\":\"tx008\",\"userId\":\"april\",\"amount\":80.0,\"timestamp\":1700002100},"
                + "{\"transactionId\":\"tx009\",\"userId\":\"dan\",\"amount\":-60.0,\"timestamp\":1700002400},"
                + "{\"transactionId\":\"tx010\",\"userId\":\"jack\",\"amount\":110.0,\"timestamp\":1700002700}"
                + "]";

        Transaction[] arr = mapper.readValue(input, Transaction[].class);
        List<Transaction> transactions = Arrays.asList(arr);

        Map<String, UserMetrics> result = computeUserMetricsStream(transactions);

        // Output as pretty-printed JSON, sorted alphabetically
        String output = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(new TreeMap<>(result));

        System.out.println(output);
    }
}
