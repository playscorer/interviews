package org.example.probability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ProbabilityTest {

    private Random random;

    @BeforeEach
    public void setUp() {
        // initialize the RandomGenerator object with a list of probabilities for testing
        random = new Random();
    }

    @Test
    public void testConstructorWithNullArgument() {
        // Test that the MyClass constructor throws a NullPointerException with null argument
        assertThrows(NullPointerException.class, () -> {
            RandomGenerator myClass = new RandomGenerator(null);
        });
    }

    @Test
    public void testWithEmptyProbabilities() {
        // Test that the MyClass constructor throws a NullPointerException with empty argument
        assertThrows(NullPointerException.class, () -> {
            RandomGenerator myClass = new RandomGenerator(new ArrayList<>());
        });
    }

    @Test
    public void testWithNegativeProbabilities() {
        assertThrows(IllegalArgumentException.class, () -> {
            var testData = new ArrayList<ProbabilisticRandomGen.NumAndProbability>();
            testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.2f));
            testData.add(new ProbabilisticRandomGen.NumAndProbability(1, -0.2f));
            RandomGenerator myClass = new RandomGenerator(testData);
        });
    }

    @Test
    public void testScaledProbabilities() {
        assertThrows(IllegalArgumentException.class, () -> {
            var testData = new ArrayList<ProbabilisticRandomGen.NumAndProbability>();
            testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.2f));
            testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.2f));
            RandomGenerator myClass = new RandomGenerator(testData);
        });
    }

    @Test
    public void testNonScaledProbabilities() {
        var testData = new ArrayList<ProbabilisticRandomGen.NumAndProbability>();
        testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.2f));
        testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.2f));
        assertThrows(IllegalArgumentException.class, () -> {
            new RandomGenerator(testData, false, new Random());
        });

        testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.6f));
        new RandomGenerator(testData, false, new Random());

        testData.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.6f));
        assertThrows(IllegalArgumentException.class, () -> {
            new RandomGenerator(testData, false, new Random());
        });
    }

    @Test
    public void testSetProbabilities() {
        List<ProbabilisticRandomGen.NumAndProbability> probabilities = new ArrayList<>();
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.25f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(2, 0.5f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(3, 0.2f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(4, 0.05f));
        var random = new RandomGenerator(probabilities, true, new Random(1));
        int[] totals = new int[5];
        int totalRuns = 10000;
        for (int i1=0; i1<totalRuns; ++i1) {
            int rand = random.nextFromSample();
            assertTrue(rand >= 1 && rand <= 4);
            totals[rand]+=1;
        }
        // as random seed is contant, so will be the totals
        assertEquals(totals[1], 2470);
        assertEquals(totals[1], 4993);
        assertEquals(totals[1], 2024);
        assertEquals(totals[1], 513);
    }

    // if they supply a set of probabilities which do not sum to one, make sure the probability is relative to total
    @Test
    public void testTotalScaledProbabilities() {
        List<ProbabilisticRandomGen.NumAndProbability> probabilities = new ArrayList<>();
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(1, 0.5f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(2, 0.5f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(3, 0.25f));
        probabilities.add(new ProbabilisticRandomGen.NumAndProbability(4, 0.5f));
        var random = new RandomGenerator(probabilities, true, new Random(1));
        int[] totals = new int[5];
        int totalRuns = 10000;
        for (int i1=0; i1<totalRuns; ++i1) {
            int rand = random.nextFromSample();
            assertTrue(rand >= 1 && rand <= 4);
            totals[rand]+=1;
        }
        // as random seed is contant, so will be the totals
        assertEquals(totals[1], 2805);
        assertEquals(totals[1], 2880);
        assertEquals(totals[1], 1437);
        assertEquals(totals[1], 2878);
    }
}
