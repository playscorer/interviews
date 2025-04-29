package DSA;

import java.util.Arrays;

public class Sorting {
    
    private static void swap(int[] array, int i, int j) {
	int tmp = array[i];
	array[i] = array[j];
	array[j] = tmp;
    }
    
    /**
     * Bubble sort : n^2 - best case n
     */
    public static void bubbleSort(int[] array) {
	boolean swap=true;
	
	while (swap) {
	    swap=false;
	    for (int i=0; i<array.length-1; i++) {
		if (array[i] > array[i+1]) {
		    swap(array, i, i+1);
		    swap=true;
		}
	    }
	}
    }
    
    /**
     * Bubble sort : n^2 - best case n
     * Optimized version : every pass adds the biggest element to the end - ith pass
     */
    public static void bubbleSortOptimized(int[] array) {
	boolean swap=true;
	
	while (swap) {
	    swap=false;
	    for (int j=0; j<array.length; j++)
	    for (int i=0; i<array.length-j-1; i++) {
		if (array[i] > array[i+1]) {
		    swap(array, i, i+1);
		    swap=true;
		}
	    }
	}
    }
    
    /**
     * Selection sort : n^2 - best case n^2
     * Selects the min and swaps it to the beginning of the array - this side is considered sorted
     * and so on 
     */
    public static void selectionSort(int[] array) {
	for (int i=0; i<array.length-1; i++) {
	    int min = i;
	    
	    for (int j=i+1; j<array.length; j++) {
		if (array[j] < array[min]) {
		    min = j;
		}
	    }
	    
	    swap(array, i, min);
	}
    }
    
    /**
     * Insertion sort : n^2 - best case n
     * It is a simple sorting algorithm that works the way we sort playing cards in our hands.
     * This version performs a swap with at every iteration
     * @param array
     */
    public static void insertionSort(int[] array) {
	for (int i=1; i<array.length; i++) {
	    for (int j=i; j>0; j--) {
		if (array[j] < array[j-1]) {
		    swap(array, j, j-1);
		}
	    }
	}
    }
    
    /**
     * Insertion sort : n^2 - best case n
     * Slightly optimized. It does not do swaps but only shifts the values until find the right place to insert the key.
     * @param array
     */
    public static void insertionSortOptimized(int[] array) {
	for (int i=1; i<array.length; i++) {
	    int key = array[i];
	    int j=i-1;
	    while (j>=0 && array[j] > key) {
		array[j+1] = array[j];
		j--;
	    }
	    array[j+1] = key;
	}
    }
    
    /**
     * QuickSort : n log n
     * The average runtime for a sorted or a nearly-sorted list is quicksort’s worst case runtime: O(n²)
     * 
     * If partition returns left as idxPivot -> [left; idxPivot-1] [idxPivot; right]
     * If partition returns right as idxPivot -> [left; idxPivot] [idxPivot+1; right]
     */
    
    public static void quickSortHoare(int[] array, int left, int right) {
	int idxPivot = partitionHoare(array, left, right);
	
	System.out.println("pivot value is " + array[idxPivot]);
	
	// sort left array
	if (left < idxPivot-1) {
	    quickSortHoare(array, left, idxPivot-1);
	}
	
	// sort right array
	if (idxPivot < right) {
	    quickSortHoare(array, idxPivot, right);
	}
	
    }
    
    /**
     * Hoare partition scheme uses two indices that start at the ends of the
     * array being partitioned, then move toward each other, until they detect
     * an inversion: a pair of elements, one greater than or equal to the pivot,
     * one lesser or equal, that are in the wrong order relative to each other.
     * The inverted elements are then swapped. When the indices meet, the
     * algorithm stops and returns the final index.
     *
     * Hoare's scheme is more efficient than Lomuto's partition scheme because
     * it does three times fewer swaps on average, and it creates efficient
     * partitions even when all values are equal.
     * 
     * Like Lomuto's partition scheme, Hoare's partitioning also would cause
     * Quicksort to degrade to O(n2) for already sorted input, if the pivot was
     * chosen as the first or the last element. With the middle element as the
     * pivot, however, sorted data results with (almost) no swaps in equally
     * sized partitions leading to best case behavior of Quicksort, i.e. O(n
     * log(n)).
     */
    private static int partitionHoare(int[] array, int left, int right) {
	int pivot = array[(left + right) / 2]; // we can pick the left as pivot too
	
	System.out.println("pivot value is " + pivot);
	System.out.println("left is " + array[left]);
	System.out.println("right is " + array[right]);
	
	while (left <= right) {
	    while (array[left] < pivot) {
		left++;
		//System.out.println("left is now pointing to: " + array[left]);
	    }
	    
	    while (array[right] > pivot) {
		right--;
		//System.out.println("right is now pointing to: " + array[right]);
	    }
	    
	    if (left <= right) {
		System.out.println("now swapping left and right pointers: " + array[left] + " " + array[right]);
		
		swap(array, left, right);
		left++;
		right--;
		
		//System.out.println("left is now pointing to: " + array[left]);
		//System.out.println("right is now pointing to: " + array[right]);
	    }
	}
	
	return left;
    }
    
    /**
     * QuickSort : n log n
     * The average runtime for a sorted or a nearly-sorted list is quicksort’s worst case runtime: O(n²)
     */
    
    public static void quickSortLomuto(int[] array, int left, int right) {
	int idxPivot = partitionLomuto(array, left, right);
	
	System.out.println("pivot value is " + array[idxPivot]);
	
	// sort left array
	if (left < idxPivot-1) {
	    quickSortLomuto(array, left, idxPivot-1);
	}
	
	// sort right array
	if (idxPivot+1 < right) {
	    quickSortLomuto(array, idxPivot+1, right);
	}
	
    }
    
    /**
     * Lomuto scheme chooses a pivot that is typically the last element in the
     * array. The algorithm maintains index i as it scans the array using
     * another index j such that the elements left through i-1 (inclusive) are
     * less than the pivot, and the elements i through j (inclusive) are equal
     * to or greater than the pivot. As this scheme is more compact and easy to
     * understand, it is frequently used in introductory material, although it
     * is less efficient than Hoare's original scheme. This scheme degrades to
     * O(n2) when the array is already in order.
     */
    private static int partitionLomuto(int[] array, int left, int right) {
	int pivot = array[right];
	
	System.out.println("pivot value is " + pivot);
	System.out.println("left/i is " + array[left]);
	System.out.println("right is " + array[right-1]);
	
	int i=left; // smaller value
	for (int j=left; j<right; j++) {
	    System.out.println("j is now pointing to: " + array[j]);

	    if (array[j] < pivot) {
		System.out.println("now swapping i and j pointers: " + array[i] + " " + array[j]);
		
		swap(array, i, j);
		i++;
		
		System.out.println("i is now pointing to: " + array[i]);
	    }
	}

	System.out.println("now swapping i and right pointers: " + array[i] + " " + array[right]);
	swap(array, i, right);
	
	return i;
    }
    
    /**
     * MergeSort: n log n
     * 
     * Space Complexity: n (we use n space for every recursion) If you draw the
     * space tree out, it will seem as though the space complexity is O(nlogn).
     * However, as the code is a Depth First code, you will always only be
     * expanding along one branch of the tree.
     */
    public static void mergeSort(int[] array) {
	if (array.length > 1) {
	    int half = array.length/2;
	    int[] leftArray = Arrays.copyOf(array, half);
	    int[] rightArray = Arrays.copyOfRange(array, half, array.length);
	    
	    System.out.println("leftArray: " + Arrays.toString(leftArray));
	    System.out.println("rightArray: " + Arrays.toString(rightArray));
	    
	    mergeSort(leftArray);
	    mergeSort(rightArray);
	    
	    merge(array, leftArray, rightArray);
	}
    }
    
    /**
     * Merge two sorted arrays.
     */
    private static void merge(int[] array, int[] leftArray, int[] rightArray) {
	int i = 0, j = 0, k = 0;
	
	while (i < leftArray.length && j < rightArray.length) {
	    if (leftArray[i] < rightArray[j]) {
		array[k++] = leftArray[i++];
	    } else {
		array[k++] = rightArray[j++];
	    }
	}

	while (i < leftArray.length) {
	    array[k++] = leftArray[i++];
	}

	while (j < rightArray.length) {
	    array[k++] = rightArray[j++];
	}
    }
    
    /**
     * Optimized MergeSort : instead of using 2 arrays per recursion this
     * algorithm only uses one single array and it uses two pointers left and
     * right.
     */
    public static void mergeSortOptimized(int[] array) {
	int[] helper = Arrays.copyOf(array, array.length);
	mergeSort(array, helper, 0, array.length);
    }
    
    /**
     * Implementation using indices for top-down merge sort algorithm that
     * recursively splits the list into sublists until sublist size is 1, then
     * merges those sublists to produce a sorted list. The copy back step is
     * avoided with alternating the direction of the merge with each level of
     * recursion.
     */
    private static void mergeSort(int[] array, int[] helper, int left, int right) {
	if (left < right-1) {
	    int middle = (left + right) / 2;
	    mergeSort(helper, array, left, middle);
	    mergeSort(helper, array, middle, right);
	    
	    merge(array, helper, left, middle, right);
	}
    }
    
    private static void merge(int[] array, int[] helper, int left, int middle, int right) {
	int i = left, j = middle, k = left;
	
	while (i < middle && j < right) {
	    if (helper[i] < helper[j]) {
		array[k++] = helper[i++];
	    } else {
		array[k++] = helper[j++];
	    }
	}

	// we only copy the remaining left array because if it had to be the right one it is already there
	while (i < middle) {
	    array[k++] = helper[i++];
	}
    }
    
    /**
     * Iterative MergeSort from bottom (arrays of size 1) to top (full array).
     */
    public static void mergeSortIterative(int[] array) {
	int length = array.length;
	int[] helper = Arrays.copyOf(array, length);
	
	// each 1-element run in array[] is already "sorted"
	// make successively longer sorted runs of length 2, 4,... until whole array is sorted.
	for (int width=1; width<length; width=2*width) {
	    System.out.println("width=" + width);

	    // array[] is full of runs of length width
	    for (int i=0; i<length; i=i+2*width) {
		System.out.print(" |" + i + " " + Math.min(length, (i+width)) + " " + Math.min(length, (i+2*width)) + "| ");
		
		// merge two runs: helper[i:i+width-1] and helper[i+width:i+2*width-1] to array[]
		// or copy helper[i:length-1] to array[] ( if(i+width >= length) )
		merge(array, helper, i, Math.min(length, (i+width)), Math.min(length, (i+2*width)));

		// now helper[] is full of runs of length 2*width
		// copy helper[] to array[] for next iteration
		helper = Arrays.copyOf(array, length);
		
		// now helper[] is full of runs of length 2*width
	    }
	    
	    System.out.println();
	}
    }
    
    public static void main(String[] args) {
	int[] array = {38, 27, 43, 3, 9, 82, 10, 15, 12};
	Sorting.mergeSortIterative(array);
	
	System.out.println(Arrays.toString(array));
	
    }

}
