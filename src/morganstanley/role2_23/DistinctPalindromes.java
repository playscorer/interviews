package morganstanley.role2_23;

import java.util.HashSet;
import java.util.Set;

public class DistinctPalindromes {

    /*
        Return all the distinct palindromes for the given string.
        input : String
        output : Number of distinct palindromes

        Ex: abbaba -> 6
            a
            b
            bb
            bab
            aba
            abba
     */

    static boolean isPalindrome(String str) {
        int length = str.length();
        int beginIdx = 0;
        int endIdx = length-1;

        while(beginIdx < endIdx
                && str.charAt(beginIdx) == str.charAt(endIdx)) {

            beginIdx++;
            endIdx--;
        }

        if (beginIdx >= endIdx)
            return true;

        return false;

    }

    static int findDistinctPalindromes(String str) {
        Set<String> palindromesSet = new HashSet<>();

        for (int begin=0; begin<str.length(); begin++) {
            int end = begin+1;
            while (end<=str.length()) {
                String subStr = str.substring(begin, end);
                if (isPalindrome(subStr)) {
                    palindromesSet.add(subStr);
                }
                end++;
            }
        }

        return palindromesSet.size();
    }

    public static void main(String[] args) {
        System.out.println("abbaba: " + findDistinctPalindromes("abbaba"));
    }
}
