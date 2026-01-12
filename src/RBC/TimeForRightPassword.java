package RBC;

import java.util.*;

/**
    k is the number tries after which there is a 5s wait time
    Every password entry costs 1s
    Passwords are entered in increasing length
    Passwords of same length can be entered in any order
    Return the best and worst case

    Ex1: k=1, password=aa1, entries=aa1,bbb
         result=1,7 (1 for best case, 7 = 1 + 5 + 1 for worst case)

    Ex2: k=2, password=aa1, entries=a,b,c,d,aa1,bbb,ccc
         result=15,22
         k=1, result=25,37
         k=3, result=10,17
         k=4, result=10,12
         k=5, result=5,12

    Ex3: k=2, password=aa1, entries=a,b,c,d,bb,cc,aa1,bbb,ccc
         result=22,29
 */
public class TimeForRightPassword {
    // from what I figured out :
    // for pwds of len < len(pwd), best/worst = nb(pwds of len < len(pwd)) + nb(pwds of len < len(pwd))/k * 5
    // for pwds of len = len(pwd), best = best + 1
    // for pwds of len = len(pwd), worst = nb(pwds of len = len(pwd)) + nb(pwds of len = len(pwd)) / k * 5 (if k>1)
    // for pwds of len = len(pwd), worst = nb(pwds of len = len(pwd)) + nb-1(pwds of len = len(pwd)) / k * 5 (if k=1)

    public static List<Integer> solve(int k, List<String> passwords, String password) {
        Collections.sort(passwords);
        Map<Integer, Integer> mapCount = new TreeMap<>();
        int len = password.length();
        int nbPwdsByLength=0;
        int best=0;
        int worst=0;

        for (String pwd : passwords) {
            mapCount.put(pwd.length(), mapCount.getOrDefault(pwd.length(), 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : mapCount.entrySet()) {
            if (entry.getKey() < len) {
                nbPwdsByLength += entry.getValue();
            } else {
                best = nbPwdsByLength + nbPwdsByLength / k * 5 + 1;
                nbPwdsByLength += entry.getValue();

                if (k > 1) worst = nbPwdsByLength + nbPwdsByLength / k * 5;
                else if (k == 1) worst = nbPwdsByLength + (nbPwdsByLength - 1) / k * 5;

                break;
            }
        }

        return List.of(best, worst);
    }

    public static void main(String[] args) {
        System.out.println(
                TimeForRightPassword.solve(5,
                        Arrays.asList(new String[]{"a","b","c","d","aa1","bbb","ccc"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(4,
                        Arrays.asList(new String[]{"a","b","c","d","aa1","bbb","ccc"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(3,
                        Arrays.asList(new String[]{"a","b","c","d","aa1","bbb","ccc"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(2,
                        Arrays.asList(new String[]{"a","b","c","d","aa1","bbb","ccc"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(1,
                        Arrays.asList(new String[]{"a","b","c","d","aa1","bbb","ccc"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(1,
                        Arrays.asList(new String[]{"aa1","bbb"}),
                        "aa1"));
        System.out.println(
                TimeForRightPassword.solve(2,
                        Arrays.asList(new String[]{"a","b","c","d","bb","cc","aa1","bbb","ccc"}),
                        "aa1"));
    }
}
