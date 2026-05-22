package gs.prep.nyc2026;

/**
 * I find my solution more intuitive than the one I coded on Leetcode.
 */
public class SearchInRotatedArray {

    public static int solution(int[] nums, int target) {
        int left=0;
        int right=nums.length-1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) return mid;

            // on the right side
            if (nums[mid] < nums[nums.length-1]) {
                if (nums[mid] <= target && target <= nums[nums.length-1]) left = mid +1;
                else right= mid-1;
            }
            // left side
            else {
                if (nums[0] <= target && nums[mid] >= target) right = mid - 1;
                else left=mid+1;
            }
        }

        return -1;
    }
}
