package DSA;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PalindromePermut {
    
    public static boolean isIt(String str) {
	Map<Character, Integer> map = new HashMap<>();
	
	for (int i=0; i<str.length(); i++) {
	    char c = str.charAt(i);
	    
	    Integer counter = map.get(c);
	    if (counter == null) {
		map.put(c, 1);
	    } else {
		map.put(c, ++counter);
	    }
	}
	
	int odd = 0;
	for (Entry<Character, Integer> entry : map.entrySet()) {
	    if (entry.getValue() % 2 != 0) {
		if (odd == 0) {
		    odd++;
		} else {
		    return false;
		}
	    }
	}
	
	System.out.println("Map:= " + map);
	
	return true;
    }

    public static void main(String[] args) {
	System.out.println(PalindromePermut.isIt("tacocat"));
	System.out.println(PalindromePermut.isIt("taocact"));
	System.out.println(PalindromePermut.isIt("taoact"));
	System.out.println(PalindromePermut.isIt("tacoat"));
	
	System.out.println(Character.getNumericValue('b'));
	System.out.println(Character.getNumericValue('B'));
    }

}
