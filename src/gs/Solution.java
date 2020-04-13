package gs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
**  Instructions:
**
**  Given a list of student test scores, find the best average grade.
**  Each student may have more than one test score in the list.
**
**  Complete the bestAverageGrade function in the editor below.
**  It has one parameter, scores, which is an array of student test scores.
**  Each element in the array is a two-element array of the form [student name, test score]
**  e.g. [ "Bobby", "87" ].
**  Test scores may be positive or negative integers.
**
**  If you end up with an average grade that is not an integer, you should
**  use a floor function to return the largest integer less than or equal to the average.
**  Return 0 for an empty input.
**
**  Example:
**
**  Input:
**  [ [ "Bobby", "87" ],
**    [ "Charles", "100" ],
**    [ "Eric", "64" ],
**    [ "Charles", "22" ] ].
**
**  Expected output: 87
**  Explanation: The average scores are 87, 61, and 64 for Bobby, Charles, and Eric,
**  respectively. 87 is the highest.
*/

class Solution {

    private static Map<String, Double> grades = new HashMap<>();

    private static Map<String, Integer> tests = new HashMap<>();

    /*
     ** Find the best average grade.
     */
    public static Integer bestAverageGrade(String[][] scores) {
	// TODO: implement this function
	for (int i = 0; i < scores.length; i++) {
	    String student = scores[i][0];
	    Double mark = Double.valueOf(scores[i][1]);

	    if (grades.get(student) == null) {
		grades.put(student, mark);
		tests.put(student, 1);

	    } else {
		Double previousMark = grades.get(student);
		grades.put(student, previousMark + mark);
		tests.put(student, tests.get(student) + 1);
	    }
	}

	grades.forEach((k, v) -> grades.put(k, v / tests.get(k)));

	Optional<Map.Entry<String, Double>> result = grades.entrySet().stream()
		.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).findFirst();

	System.out.println(result);

	if (result.isPresent()) {
	    return (int) Math.floor(result.get().getValue());
	}

	return 0;
    }

    /*
     ** Returns true if the tests pass. Otherwise, returns false;
     */
    public static boolean doTestsPass() {
	// TODO: implement more test cases
	String[][] tc1 = { { "Bobby", "87" }, { "Charles", "100" }, { "Eric", "64" }, { "Charles", "22" } };

	return bestAverageGrade(tc1) == 87;
    }

    /*
     ** Execution entry point.
     */
    public static void main(String[] args) {
	// Run the tests
	if (doTestsPass()) {
	    System.out.println("All tests pass");
	} else {
	    System.out.println("Tests fail.");
	}
    }
}
