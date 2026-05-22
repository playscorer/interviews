package gs.prep.nyc2026;

import java.util.HashSet;
import java.util.Set;

/**
 * Suppose I have "abbbccda" then it should return [1, 3].
 * Because it starts from index 1 and is 3 characters long.
 * If the input string is empty then return [-1, 0].
 * Other Examples: "10000111" => [ 1, 4 ] "aabbbbbCdAA" => [ 2, 5 ]
 */
public class LongestUniformSubstring {

    public static int[] longestUniformSubstringMine(String s) {
        int left=0,right=0;
        int max = 0;
        int l=0;

        if (s == null || s.isEmpty()) return new int[]{-1, 0};

        for (right=0; right<s.length(); right++) {
            char c = s.charAt(right);
            System.out.println("left: " + left + " right: " + right + " char(left): " + s.charAt(left) + " char(right): " + s.charAt(right));

            while (s.charAt(left) != c) {
                if (max < right - left) {
                    max = right - left;
                    l = left;
                }
                left++;
                System.out.println("left: " + left);
            }
        }

        if (max < right - left) {
            max = right - left;
            l = left;
        }

        return new int[] {l, max};
    }

    public static int[] longestUniformSubstring(String s) {
        int l=0, max=0, start=0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != s.charAt(i-1)) {
                if (max < i - start) {
                    max = i - start;
                    l = start;
                }
                start = i;
            }
        }
        if (max < s.length() - start) {
            max = s.length() - start;
            l = start;
        }

        return new int[] {l, max};
    }

    public static int[] longestUniformSubstringX(String s) {
        int l=0, max=0, start=0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != s.charAt(start)) {
                if (max < i - start) {
                    max = i - start;
                    l = start;
                }
                start = i;
            }
        }
        if (max < s.length() - start) {
            max = s.length() - start;
            l = start;
        }

        return new int[] {l, max};
    }

    public static void main(String[] args) {
        int[] x = longestUniformSubstringX("abbbccda");
        System.out.println("res: " + x[0] + ", " + x[1]);
        x = longestUniformSubstringX("abccccc");
        System.out.println("res: " + x[0] + ", " + x[1]);
    }

}
