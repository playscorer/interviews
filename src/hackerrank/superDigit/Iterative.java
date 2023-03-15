package hackerrank.superDigit;

public class Iterative {

    /**
     * We define super digit of an integer x using the following rules:
     *
     * Given an integer, we need to find the super digit of the integer:
     *  If x has only 1 digit, then its super digit is x.
     *  Otherwise, the super digit of x is equal to the super digit of the sum of the digits of x.
     *
     * Example:
     *  super_digit(9875)   9+8+7+5 = 29
     *  super_digit(29) 	2 + 9 = 11
     * 	super_digit(11)		1 + 1 = 2
     * 	super_digit(2)		= 2
     *
     * The number p is created by concatenating the string n k times so the initial p=9875987598759875.
     *
     */

    public static int superDigit(String n, int k) {
        int newDigit=0;
        int superDigit=0;

        for (int i=0; i<n.length(); i++) {
            int digit = Character.getNumericValue(n.charAt(i));
            newDigit = reduceDigit(newDigit + digit);
        }

        superDigit = newDigit;

        for (int i=1; i<k; i++) {
            superDigit = reduceDigit(newDigit + superDigit);
        }

        return superDigit;
    }

    private static int reduceDigit(int superDigit) {
        if (superDigit > 10) {
            superDigit = 1 + superDigit % 10;
        }
        return superDigit;
    }

    public static void main(String[] args) {
        System.out.println(superDigit("4757362", 10000));
    }

}
