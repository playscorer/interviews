package DSA;

public class BinarySearch {
    
    public static int search(int[] array, int target) {
		int left=0;
		int right=array.length-1;

		while (left <= right) {
			System.out.println("search: " + left + " " + right);
			int mid = (left + right) / 2;

			if (target < array[mid]) {
				right = mid-1;
			} else if (target > array[mid]) {
				left = mid + 1;
			} else {
				return mid;
			}
		}

		return -1;
    }
    
    public static int recursiveSearch(int[] array, int begin, int end, int value) {
		System.out.println("recursiveSearch: " + begin + " " + end);

		int mid = (begin + end) / 2;
		if (value < array[mid]) {
			return recursiveSearch(array, begin, mid-1, value);
		}
		else if (value > array[mid]) {
			return recursiveSearch(array, mid+1, end, value);
		}
		else {
			return mid;
		}
    }

    public static void main(String[] args) {
		int[] array = new int[100];
		for (int i=1; i<=array.length; i++) {
			array[i-1] = i;
		}
		System.out.println(search(array, 49));
    }

}
