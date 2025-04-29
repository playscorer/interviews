package DSA;

public class SecondSmallestValue {

    public static int getSecSmallValue(int[] values) {
	int min=Integer.MAX_VALUE;
	int secmin=min;
	
	for (int i=0; i<values.length; i++) {
	    if (min > values[i]) {
		secmin = min;
		min = values[i];
	    } else if (values[i] > secmin && values[i] < min) {
		secmin = values[i];
	    }
	}
	
	return secmin;
    }
    
    public static void main(String[] args) {
	int[] numbers = {2, 7, 1, 3, 5, 0};
	System.out.println("Second min value: " + SecondSmallestValue.getSecSmallValue(numbers));
    }
    
}
