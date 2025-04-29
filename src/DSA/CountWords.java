package DSA;

import java.util.HashMap;
import java.util.Map;

public class CountWords {
    
    private String sentence;
    
    public CountWords(String sentence) {
	this.sentence = sentence;
    }
    
    /**
     * Solution O(n^2)
     */
    public void count() {
	String[] words;
	int cpt=1;
	
	if (sentence != null && !sentence.isEmpty()) {
	    words = sentence.split("\\s");
	    
	    for (int i=0; i<words.length; i++) {
		for (int j=i+1; j<words.length; j++) {
		    if (!words[i].equals("0") && !words[j].equals("0") && words[i].equals(words[j])) {
			cpt++;
			words[j] = "0";
		    }
		}
		if (!words[i].equals("0")) {
		    System.out.println(words[i] + " - " + cpt);
		}
		cpt=1;
	    }
	}
    }
    
    /**
     * Solution O(n)
     */
    public void countSol2() {
	String[] words;
	Map<String, Integer> countWords = new HashMap<>();
	
	if (sentence != null && !sentence.isEmpty()) {
	    words = sentence.split("\\s");
	    
	    for (int i=0; i<words.length; i++) {
		countWords.put(words[i], countWords.getOrDefault(words[i], 0) + 1);
	    }
	}
	
	System.out.println(countWords);
    }

    public static void main(String[] args) {
	CountWords words = new CountWords("Java Java Java To");
	words.count();
	words.countSol2();
    }

}
