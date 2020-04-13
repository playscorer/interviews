package algorithm;

public class GenerateLettersSeq {
    
    private static String convert(int number, int base) {
	StringBuilder builder = new StringBuilder();

	do {
	    int digit = number % base;
	    builder.insert(0, (char) (digit + 'A'));
	    number = number / base;
	} while (number != 0);
	
	return builder.toString();
    }
    
    public static void generateSeq(int base, int length) {
	for (int i=0; i<Math.pow(base, length); i++) {
	    String str = convert(i, base);
	    str = padString(str, length);
	    System.out.println(str);
	}
    }

    private static String padString(String str, int length) {
	StringBuilder builder = new StringBuilder(str);
	while (builder.length() < length) {
	    builder.insert(0, 'A');
	}
	return builder.toString();
    }

    public static void main(String[] args) {
	generateSeq(26, 4);
    }

}
