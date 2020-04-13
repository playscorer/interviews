package Disney;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * input: f: 0, i: 1, l: 2, i: 3, p: 4, e: 5
 * output:  e f i i l p -> e: 5, f: 0, i: 1, i: 3, l: 2, p: 4
 */
public class StringProblem {
    
    /*
     * Constructs the map Character -> List of Indexes
     */
    public static TreeMap<Character, List<Integer>> constructMap(String str) {
	TreeMap<Character, List<Integer>> sortedMap = new TreeMap<>();
	int length = str.length();

	for (int idx=0; idx<length; idx++) {
	    Character c = str.charAt(idx);
	    
	    List<Integer> indexesForLetter = sortedMap.get(c);
	    if (indexesForLetter == null) {
		indexesForLetter = new ArrayList<>();
		sortedMap.put(c, indexesForLetter);
	    }
	    
	    indexesForLetter.add(idx);
	}
	return sortedMap;
    }
    
    public static void displayIndex(String str) {
	TreeMap<Character, List<Integer>> sortedMap = constructMap(str);
	
	for (Entry<Character, List<Integer>> entry : sortedMap.entrySet()) {
	    Character key = entry.getKey();
	    
	    if (entry.getValue().size() == 1) {
		System.out.println(key + ": " + entry.getValue().get(0));
	    } else  {
		for (Integer indexForKey : entry.getValue()) {
		    System.out.println(key + ": " + indexForKey);
		}
	    }
	}
    }
    
    public static void main(String[] args) {
	StringProblem.displayIndex("hello");
    }
}
