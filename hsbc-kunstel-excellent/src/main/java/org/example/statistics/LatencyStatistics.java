package org.example.statistics;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LatencyStatistics implements SlidingWindowStatistics {

    private int[] measurements;
    private int windowSize;
    private long samples;

    private AtomicLong subcount = new AtomicLong();

    private ConcurrentHashMap<Long, StatisticsCallback> subscribers = new ConcurrentHashMap<>();

    public LatencyStatistics(int windowSize) {
        this.measurements = new int[windowSize];
        this.windowSize = windowSize;
        samples = 0;
    }

    public synchronized void add(int measurement) {
        int pos = (int) samples++ % windowSize;
        measurements[pos] = measurement;
        if (samples >= windowSize) {
            onStatisticsUpdated(getLatestStatistics());
        }
    }

    private void onStatisticsUpdated(Statistics latestStatistics) {
        var it = subscribers.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().onStatisticsUpdated(latestStatistics);
        }
    }

    @Override
    public long subscriberForStatistics(StatisticsCallback callback) {
        var cnt = subcount.incrementAndGet();
        subscribers.put(cnt, callback);
        return cnt;
    }

    @Override
    public Statistics getLatestStatistics() {
        double sum = Arrays.stream(measurements).sum();
        long cnt = (samples>windowSize) ? windowSize : samples;
        if (cnt == 0) {
            return null;
        }
        double mean = sum / cnt;

        double variance = Arrays.stream(measurements).limit(cnt).mapToDouble(val -> Math.pow(val - mean, 2)).sum() / cnt;
        return new Statistics(mean, variance, cnt);
    }
}
