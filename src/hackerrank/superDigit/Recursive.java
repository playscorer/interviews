package hackerrank.superDigit;

public class Recursive {

    public static int superDigit(String n, int k) {
        long superDigit = 0;
        for(int i=0; i<n.length(); i++) {
            superDigit += Character.getNumericValue(n.charAt(i));
        }
        superDigit = sumDigit(superDigit * k);
        return (int) sumDigit(superDigit);
    }

    private static long sumDigit(long n) {
        if (n<10) {
            return n;
        } else {
            return n % 10 + sumDigit(n / 10);
        }
    }

    public static void main(String[] args) {
        System.out.println(superDigit("4757362", 10000));
    }

}
