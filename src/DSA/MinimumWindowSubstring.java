package DSA;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * HARD Problem: After reading the solution I cannot still figure it out.
 * This Algorithm passed 60% of the test cases.
 */
public class MinimumWindowSubstring {
    public static String minWindow(String s, String t) {
        int l=0;
        int r=0;
        int ans[] = {-1, 0, 0};

        Set<Character> dictT = new HashSet<>();
        for (int i=0; i<t.length(); i++) {
            char c = t.charAt(i);
            dictT.add(c);
        }
        int required = dictT.size();

        Map<Character, Integer> windowCounts = new HashMap<>();
        while (r < s.length()) {
            char c = s.charAt(r);
            if (dictT.contains(c)) {
                windowCounts.put(c, windowCounts.getOrDefault(c, 0) + 1);
            }
            if (windowCounts.size() == required) {
                System.out.println(l + " " + r);
                // reduction
                while (l<s.length() && (!dictT.contains(s.charAt(l)) || windowCounts.getOrDefault(s.charAt(l), 0) > 1)) {
                    c = s.charAt(l);
                    if (dictT.contains(c)) windowCounts.put(c, windowCounts.get(c)-1);
                    l++;
                }
                System.out.println(windowCounts);
                if (ans[0]==-1 || r-l+1 < ans[0]) {
                    ans[0] = r-l+1;
                    ans[1] = l;
                    ans[2] = r;
                }
                System.out.println(ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1));
                if (dictT.contains(s.charAt(l))) {
                    windowCounts.put(s.charAt(l), windowCounts.get(s.charAt(l))-1);
                    if (windowCounts.get(s.charAt(l)) == 0) windowCounts.remove(s.charAt(l));
                }
                System.out.println(windowCounts);
                l++;
            }
            r++;


        }

        return ans[0] == -1 ? "" : s.substring(ans[1], ans[2] + 1);
    }

    public static void main(String[] args) {
        String str = MinimumWindowSubstring.minWindow("ADOBECODEBANC", "ABC");
        System.out.println("str:= " + str);
    }
}