package morganstanley.role2_23;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DistinctPalindromes {

    /*
        First Problem :
            Given cubes of 1cm width and height, how many cubes are needed to complete a 5cm width side cube? 5^3 = 125
            How many cubes would get covered if the cube is submerged in a full bucket of painting? 5^3 - 3^3 = 98
            Can you generalize the formula? n^3 - (n-2)^3
            For which values does this formula work? n >= 2
    */

    /*
        Second Problem :
           On a STP system, how would you process the incoming trading orders? Queues
           How to avoid duplicates? Unique orderId
           Which structure would you use? Set
           How to avoid space problems? Use of a LRU cache (Least Recently Used) then persisted to DB
           How have you solved the duplicate problem? If not present in Cache lookup orderId in DB
    */

    /*
        Third Problem :
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

    /*
        Second Round :
            Write a program that sums a list of integers
            What if values are null? Add if condition
            What problem can we encounter? Size overflow (strongest bit is signed) -> check whether it is incrementing or decrementing based on the value (positive or negative)
            What are the impacts in time complexity if an array is used instead of a list? None because we are iterating through all the elements and not accessing one particular element.
            How to multithread? Use for instance availableProcessors() threads and partition the list based on the number of threads.
            Synchronization in Java will only be needed if a shared object is mutable. If your shared object is either a read-only or immutable object, then you don't need synchronization, despite running multiple threads.
                Read more: https://javarevisited.blogspot.com/2011/04/synchronization-in-java-synchronized.html#ixzz82Tq4aBKN
     */
    static Integer sum(List<Integer> integers) {
        int sum=0;
        int prevSum=0;

        for (Integer i : integers) {
            if (i != null) {
                sum += i;

                if (i>0 && sum < prevSum || i<0 && sum > prevSum)
                    throw new RuntimeException();

                prevSum += i;

            } else throw new NullPointerException();
        }

        return sum;
    }

    /*
        Third Round :
            We have 4 persons, A, B, C and D that are crossing a bridge between NY and NJ but only 2 at time can cross the bridge holding one single torch. There is only one torch.
            A -> takes 10hrs to cross the bridge
            B -> takes 5hrs to cross the bridge
            C -> takes 2hrs to cross the bridge
            D -> takes 1hr to cross the bridge

            Send C and D : 2hrs
            D is back : 1hr (C)
            Send A and B : 10hrs
            C is back : 2hrs (A, B)
            Send C and D : 2hrs (A, B, C, D)

            TOTAL = 17hrs
     */

    public static void main(String[] args) {
        System.out.println("abbaba: " + findDistinctPalindromes("abbaba"));

        Integer[] integers = {1, 2, -4, 3, 5, -7, -8, 9};
        System.out.println("Sum of : " + Arrays.stream(integers).toList() + " = " + sum(Arrays.stream(integers).toList()));
    }
}
