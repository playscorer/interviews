package DSA;

public class BinarySearchInRotatedArray {
    public static int search(int[] nums, int target) {
        int left=0;
        int right=nums.length-1;

        while (left <= right) {
            int mid=(left+right)/2;

            if (nums[mid] == target) {
                return mid;
            }

            // left sorted portion
            if (nums[left] <= nums[mid]) {
                if (target > nums[mid] || target < nums[left]) {
                    left = mid+1;
                } else { // if (target <= nums[mid] && target >= nums[left])
                    right = mid-1;
                }
            }

            // right sorted portion
            else {
                if (target < nums[mid] || target > nums[right]) {
                    right = mid-1;
                } else { // if (target >= nums[mid] && target <= nums[right])
                    left = mid+1;
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] array = {7, 0, 1, 2, 3, 4, 5, 6};
        System.out.println(BinarySearchInRotatedArray.search(array, 7));
        System.out.println(BinarySearchInRotatedArray.search(array, 3));
    }
}
