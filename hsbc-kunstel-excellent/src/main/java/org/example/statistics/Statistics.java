package org.example.statistics;

public class Statistics {

    private double mean;
    private double variance;
    private long count;

    public Statistics(double mean, double variance, long count) {
        this.mean = mean;
        this.variance = variance;
        this.count = count;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

}
