package capitalone;

/**
 * Jeffersonton, VA - May 22th, 2026 - Online Assessment
 * Solved it in 9mn
 *
 * Given a string abcdef, return afbecd
 *                abcde, return aebdc
 */
public class Exercise1 {

    public static String solution(String s) {
        int left=0;
        int right=s.length();
        StringBuilder sb = new StringBuilder();

        while (left < right) {
            sb.append(s.charAt(left));
            sb.append(s.charAt(right));
            left++;
            right--;
        }

        if (left == right) {
            sb.append(s.charAt(left));
        }

        return sb.toString();
    }
}
