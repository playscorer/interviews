package DSA;

public class SecondBiggestValue {
    
    public static int getSecBigValue(int[] values) {
	int max=0;
	int secmax=0;
	
	for (int i=0; i<values.length; i++) {
	    if (max < values[i])
		max = values[i];
	}
	for (int i=0; i<values.length; i++) {
	    if (secmax < values[i] && values[i] < max)
		secmax = values[i];
	}
	
	return secmax;
    }
    
    public static int getSecBigValue2(int[] values) {
	int max=0;
	int secmax=0;
	
	for (int i=0; i<values.length; i++) {
	    if (max < values[i]) {
		secmax = max;
		max = values[i];
	    } else if (values[i] > secmax && values[i] < max) {
		secmax = values[i];
	    }
	}
	
	return secmax;
    }
    
    public static void main(String[] args) {
	int[] numbers = {2, 1, 3, 5, 0};
	
	System.out.println("Second max value: " + SecondBiggestValue.getSecBigValue(numbers));
	System.out.println("Second max value: " + SecondBiggestValue.getSecBigValue2(numbers));
	
	numbers = new int[] {2, 7, 1, 3, 5, 0};
	
	System.out.println("Second max value: " + SecondBiggestValue.getSecBigValue2(numbers));
    }

}
