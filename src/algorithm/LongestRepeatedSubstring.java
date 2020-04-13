package algorithm;

/**
 * aaabbbbccc => bbbb, 4
 * 
 * @author Filipe
 *
 */
public class LongestRepeatedSubstring {
    
    public static String find(String s) {
	int length=0;
	StringBuilder builder;
	String longestString = null;
	char[] arrayS = s.toCharArray();
	
	for (int i=0; i<s.length()-1; i++) {
	    int j=i+1;
	    int current = 0;
	    char a = arrayS[i];
	    char b = arrayS[j];
	    builder = new StringBuilder();
	    
	    while (a == b && i<s.length()-1 && j<s.length()-1) {
		builder.append(a);
		i++;
		j++;
		a = arrayS[i];
		b = arrayS[j];
		current++;
	    }
	    
	    builder.append(a);
	    current++;

	    if (j == s.length() - 1 && a == b) {
		builder.append(b);
		current++;
	    }

	    if (length < current) {
		length = current;
		longestString = builder.toString();
	    }

	}
	
	return longestString;
    }
    
    public static void main(String[] args) {
	String str = LongestRepeatedSubstring.find("aaabbbbcccdddddddddddee");
	System.out.println("str:= " + str);
	str = LongestRepeatedSubstring.find("abaaabac");
	System.out.println("str:= " + str);
	str = LongestRepeatedSubstring.find("abbcccdddde");
	System.out.println("str:= " + str);
	str = LongestRepeatedSubstring.find("bbb");
	System.out.println("str:= " + str);	
    }

}
