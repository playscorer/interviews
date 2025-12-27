package DSA;

public class BinarySearchMinInRotatedArray {
    public static int findMin(int[] nums) {
        int low=0;
        int high=nums.length-1;

        if (nums.length == 1 || nums[0]<nums[high]) {
            return nums[0];
        }

        while (low <= high) {
            int mid= (low + high) / 2;
            System.out.println(low + " " + mid + " " + high);

            if (nums[mid] > nums[mid+1]) return nums[mid+1];
            if (nums[mid-1] > nums[mid]) return nums[mid];

            if (nums[mid] > nums[0]) {
                low = mid+1;
            } else {
                high = mid-1;
            }
        }

        System.out.println(low + " " + high);

        return nums[low];
    }
    public static int findMin2(int[] nums) {
        int left=0;
        int right=nums.length-1;

        while (left <= right) {
            int mid = (left + right) / 2;
            System.out.println(left + " " + mid + " " + right);

            //if (nums[mid] >= nums[0]) {
            if (nums[mid] > nums[nums.length-1]) {
                left = mid+1;
            } else {
                right = mid-1;
            }
        }

        System.out.println(left + " " + right);

        return nums[left];
    }

    public static void main(String[] args) {
        int[] array = {4, 5, 0, 1, 2, 3};
        System.out.println(BinarySearchMinInRotatedArray.findMin(array));
        System.out.println(BinarySearchMinInRotatedArray.findMin2(array));
        System.out.println();

        array = new int[]{1, 2, 3, 4, 5, 0};
        System.out.println(BinarySearchMinInRotatedArray.findMin(array));
        System.out.println(BinarySearchMinInRotatedArray.findMin2(array));
    }
}
