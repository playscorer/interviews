package org.example.statistics;

import java.util.function.Consumer;
import java.util.function.Function;

// class that takes a matcher/filter and if true, runs some callback code
public class ConditionalStatisticsCallback implements SlidingWindowStatistics.StatisticsCallback {

    private Function<Statistics, Boolean> matcher;
    private Consumer<Statistics> consumer;

    public ConditionalStatisticsCallback(Function<Statistics, Boolean> matcher, Consumer<Statistics> consumer) {
        this.matcher = matcher;
        this.consumer = consumer;
    }


    @Override
    public void onStatisticsUpdated(Statistics stats) {
        if (matcher.apply(stats)) {
            onMatches(stats);
        }
    }

    protected void onMatches (Statistics stats) {
        consumer.accept(stats);
    }
}
