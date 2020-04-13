package algorithm;

public class BinarySearch {
    
    public static int search(int[] array, int value) {
	int begin=0;
	int end=array.length-1;
	
	while (begin <= end) {
	    System.out.println("search: " + begin + " " + end);
	    int mid = (begin + end) / 2;

	    if (value < array[mid]) {
		end = mid-1;
	    } else if (value > array[mid]) {
		begin = mid + 1;
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
