package gs.prep.nyc2026;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Input:  [{"Bob","87"}, {"Mike", "35"},{"Bob", "52"}, {"Jason","35"}, {"Mike", "55"}, {"Jessica", "99"}]
 * Output: 99
 * Explanation: Since Jessica's average is greater than Bob's, Mike's and Jason's average.
 *
 * Complexity - time : O(grades) - space : O(students)
 */
public class HighestAvgScore {

    public double highestScore(String[][] grades) {
        Map<String, int[]> scores = new HashMap<>(); // int[0]=sum, int[1]=count

        for (String[] grade : grades) {
            String student = grade[0];
            int newGrade = Integer.parseInt(grade[1]);

            if (scores.putIfAbsent(student, new int[] {newGrade, 1}) != null) {
                scores.get(student)[0] += newGrade;
                scores.get(student)[1]++;
            }
        }

        return scores.entrySet().stream()
                .max(Comparator.comparingDouble(entry -> ((double) entry.getValue()[0] / entry.getValue()[1])))
                .map(entry -> (int) ((double) entry.getValue()[0] / entry.getValue()[1])).orElse(0);
    }
}
