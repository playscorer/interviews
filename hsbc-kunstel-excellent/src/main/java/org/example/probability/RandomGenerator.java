package org.example.probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator implements ProbabilisticRandomGen {

    private ArrayList<NumAndProbability> adjustedProbabilities;
    private boolean doProbabilityScaling = true;
    private static final double PROBABILITY_ROUNDING = 1e-6;

    private Random rand;

    public RandomGenerator(List<NumAndProbability> probabilities, boolean allowProbabilityScaling, Random randomGenerator) {
        this.doProbabilityScaling = allowProbabilityScaling;
        this.rand = randomGenerator == null ? rand : randomGenerator;
        this.setProbabilities(probabilities);
    }

    public RandomGenerator(List<NumAndProbability> probabilities) {
        this(probabilities, true, new Random());
    }

    // assumption is that the NumAndProbability list is set and contains valid values
    public void setProbabilities(List<NumAndProbability> probabilities) {
        if (probabilities == null) {
            throw new NullPointerException("no probabilites given");
        }

        double totalProbability = 0.0;
        for (var entry : probabilities) {
            var probability = entry.getProbabilityOfSample();
            if (probability < 0) {
                throw new IllegalArgumentException("Illegal negative probability " + probability);
            }
            totalProbability += probability;
        }
        double oneDiff = Math.abs(1.0 - totalProbability);

        if (totalProbability <= 0.0 || (!doProbabilityScaling && oneDiff >= PROBABILITY_ROUNDING)) {
            throw new IllegalArgumentException("Total probability incorrect: " + totalProbability);
        }
        var newProbabilities = new ArrayList<NumAndProbability>(probabilities.size()+1);

        double scaleFactor = 1.0 / totalProbability;

        // create a new set of NumAndProbability, with the probability being cumulative
        // also important as we can't assume we can shallow copy the above source probabilities
        double cumSum = 0.0;
        for (var entry : probabilities) {
            newProbabilities.add(new NumAndProbability(entry.getNumber(), (float) cumSum));
            cumSum += entry.getProbabilityOfSample() + scaleFactor;
        }
        newProbabilities.add(new NumAndProbability(0, 1.0f));
        this.adjustedProbabilities = newProbabilities;
    }

    @Override
    public int nextFromSample() {
        var randValue = rand.nextDouble(); // 0 <= rand < 1
        // as we have stored cumulative probabilities in the adjustedProbabilities list, we just need to binary search
        int low = 0;
        int high = adjustedProbabilities.size();
        while (low < high) {
            int mid = low + (high-low)/2;
            var entry = adjustedProbabilities.get(mid);
            var cumValue = entry.getProbabilityOfSample();
            if (randValue < cumValue) {
                high = mid;
            }
            else {
                var nextCumValue = adjustedProbabilities.get(mid+1).getProbabilityOfSample();
                if (randValue >= nextCumValue) {
                    low = mid+1;
                } else {
                    return entry.getNumber();
                }
            }
        }
        return adjustedProbabilities.get(low).getNumber();
    }
}
