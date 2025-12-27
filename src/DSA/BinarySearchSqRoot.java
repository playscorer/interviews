package DSA;

public class BinarySearchSqRoot {

    public static int mySqrt(int x) {
        int left=2;
        int right=x/2;

        if (x<2) {
            return x;
        }

        while (left<=right) {
            int mid = (left+right) / 2;

            // this is important
            long midXmid = (long) mid * mid;
            if (midXmid == x) {
                return mid;
            }

            if (midXmid > x) {
                right = mid-1;
            } else {
                left = mid+1;
            }
        }

        return right;
    }

    public static void main(String[] args) {
        System.out.println(BinarySearchSqRoot.mySqrt(25));
        System.out.println(BinarySearchSqRoot.mySqrt(5));
    }
}
