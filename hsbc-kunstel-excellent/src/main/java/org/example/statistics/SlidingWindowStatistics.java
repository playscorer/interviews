package org.example.statistics;

public interface SlidingWindowStatistics {

    void add(int measurement);

    long subscriberForStatistics(StatisticsCallback callback);

    Statistics getLatestStatistics();

    interface StatisticsCallback {
        void onStatisticsUpdated(Statistics stats);
    }
}
