package gs.prep.nyc2026;

public class FindSecondSmallestArray {

    public static int solution(int[] nums) {
        if (nums.length <= 1) return -1;

        int smallest = Integer.MAX_VALUE;
        int secondSmallest = Integer.MAX_VALUE;

        for (int i=0; i< nums.length; i++) {
            if (smallest > nums[i]) {
                secondSmallest = smallest;
                smallest = nums[i];
            } else if (nums[i] > smallest && secondSmallest > nums[i]) {
                secondSmallest = nums[i];
            }
        }

        // in case of duplicates [2 2 2] - second smallest never gets updated
        return secondSmallest == Integer.MAX_VALUE ? -1 : secondSmallest;
    }
}
