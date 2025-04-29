package DSA;

import java.util.Arrays;

/**
 * Given a string S, consider all duplicated substrings: (contiguous) substrings of S that occur 2 or more times.  
 * (The occurrences may overlap.)
 * Return any duplicated substring that has the longest possible length.
 * 
 * Does not work entirely
 * @author Filipe
 *
 */
public class LongestDuplicateSubstring {
    
    /**
     * My first approach 
     * Does not work with this case ababcaabcabcaab -> bcaab instead abcaab
     * @param str
     */
    public static String findAllRepeatedStrings(String str) {
	System.out.println("Input: " + str);
        int maxlength = 0;
        String res="";
        StringBuilder builder;
        // debug
        //Map<String, Integer> commonStrings = new HashMap<>();

        for (int i=0; i<str.length(); i++) {
            int j=i+1;

            while (j<str.length()) {
                int k=i;
                int length=0;
                builder = new StringBuilder();

                // move the second cursor
                while (j<str.length() && str.charAt(k) != str.charAt(j)) {
                    j++;
                }

                // move both cursors on a common string
                while (k<str.length() && j<str.length() 
                       && str.charAt(k) == str.charAt(j)) {
                    builder.append(str.charAt(k));
                    length++;
                    k++;
                    j++;
                }

                if (maxlength < length) {
                    maxlength=length;
                    res = builder.toString();
                }
                
                /* Debug */
/*		if (!builder.toString().isEmpty()) {
		    Integer nbOfTimes = commonStrings.get(builder.toString());
		    if (nbOfTimes == null) {
			commonStrings.put(builder.toString(), 2);
		    } else {
			nbOfTimes++;
			commonStrings.put(builder.toString(), nbOfTimes);
		    }
		}*/
		/**/
            }
        }
        
        //System.out.println(commonStrings);
        System.out.println("Longest: " + res);
        return res;
    }
    
    /**
     * Utility fonction that returns the longest common prefix between 2 strings 
     */
    private static String longestCommonPrefix(String s, String t) {
	int length = Math.min(s.length(), t.length());
	
	for (int i=0; i<length; i++) {
	    if (s.charAt(i) != t.charAt(i))
		return s.substring(0, i);
	}
	
	return s.substring(0, length);
    }
    
    /**
     * Brute force approach from Coursera Princeton (N^2 time complexity)
     */
    public static String longestRepeatedSubstring(String s) {
	int length = s.length();
	String lrs = "";
	
	for (int i=0; i<length; i++) {
	    for (int j=i+1; j<length; j++) {
		String prefix = longestCommonPrefix(s.substring(i), s.substring(j));
		
		if (prefix.length() > lrs.length()) {
		    lrs = prefix;
		}
	    }
	}
	
	return lrs;
    }
    
    /**
     *  Second approach using a suffix array from Coursera Princeton (NLogN time complexity)
     *  Problem with memory space because of implementation of substring method after 2012
     *  (Pr. Robert Sedgewick)
     */
    public static String lrs2(String s) {
	int length = s.length();
	String lrs = "";
	String[] suffixes = new String[length];
	
	for (int i=0; i<length; i++) {
	    suffixes[i] = s.substring(i, length);
	}
	
	Arrays.sort(suffixes);
	
	for (int i=0; i<length-1; i++) {
	    String prefix = longestCommonPrefix(suffixes[i], suffixes[i+1]);

	    if (prefix.length() > lrs.length()) {
		lrs = prefix;
	    }
	}
	
	return lrs;
    }
    
    /**
     * Suffix wrapper for third solution
     *
     */
    static class Suffix implements Comparable<Suffix> {
	private int index;
	private String text;
	
	public Suffix(int index, String text) {
	    this.index = index;
	    this.text = text;
	}

	@Override
	public int compareTo(Suffix that) {
	    int length = Math.min(this.length(), that.length());
	    
	    for (int i=0; i<length; i++) {
		if (this.charAt(i) < that.charAt(i)) {
		    return -1;
		} else if (this.charAt(i) > that.charAt(i)) {
		    return 1;
		}
	    }
	    
	    return this.length() - that.length();
	}

	public char charAt(int i) {
	    return text.charAt(index + i);
	}

	public int length() {
	    return text.length() - index;
	}

	public String substring(int end) {
	    return text.substring(index, index + end);
	}
	
    }
    
    /**
     * Reimplementation of lcp with the Suffix class
     */
    public static int lcpSuffix(Suffix s, Suffix t) {
	int length = Math.min(s.length(), t.length());
	
	for (int i=0; i<length; i++) {
	    if (s.charAt(i) != t.charAt(i)) {
		return i;
	    }
	}
	
	return length;
    }

    /**
     * Third approach using a suffix array with a wrapper object to use less memory from Coursera Princeton
     */
    public static String lrs3(String s) {
	int length = s.length();
	String lrs = "";
	Suffix[] indexes = createSuffixArray(s);
	
	for (int i=0; i<length-1; i++) {
	    int prefix = lcpSuffix(indexes[i], indexes[i+1]);

	    if (prefix > lrs.length()) {
		lrs = s.substring(indexes[i].index, indexes[i].index + prefix);
	    }
	}
	
	return lrs;
    }

    public static Suffix[] createSuffixArray(String s) {
	int length = s.length();
	Suffix[] indexes = new Suffix[length];
	
	for (int i=0; i<length; i++) {
	    indexes[i] = new Suffix(i, s);
	}
	
	Arrays.sort(indexes);
	return indexes;
    }
    
    /**
     * Utility method not used for lrs
     * Returns the rank of the given query in the suffix array 
     */
    public static int rank(String query, Suffix[] suffixes) {
        int lo = 0, hi = suffixes.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, suffixes[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }
    
    /**
     * Compare query string to suffix
     */
    private static int compare(String query, Suffix suffix) {
        int n = Math.min(query.length(), suffix.length());
        for (int i = 0; i < n; i++) {
            if (query.charAt(i) < suffix.charAt(i)) return -1;
            if (query.charAt(i) > suffix.charAt(i)) return +1;
        }
        return query.length() - suffix.length();
    }

    public static void main(String[] args) {
	System.out.println("My first approach");
	long begin = System.nanoTime();
	findAllRepeatedStrings("banana");
	findAllRepeatedStrings("abaaabac");
	findAllRepeatedStrings("abacabac");
	findAllRepeatedStrings("ababcaabcabcaab");
	findAllRepeatedStrings("abababa");
	findAllRepeatedStrings("abcabdfabababacbabacbdabdcabcdaaaabbbcccddabcdabcdabcdabababababacacacacacacdbdbdbdbdbdbdcabababacbababababacbdbacbddbcacbcbcdbdbdcabababa");
	findAllRepeatedStrings("ababcaabcabcaabababcaabcabcaabababcaabcabcaabababcaabcabcaab");
	long end = System.nanoTime();
	
	System.out.println("time: " + (end-begin));
	
	System.out.println("Brute force approach from Coursera Princeton");
	begin = System.nanoTime();
	System.out.println(longestRepeatedSubstring("banana"));
	System.out.println(longestRepeatedSubstring("abaaabac"));
	System.out.println(longestRepeatedSubstring("abacabac"));
	System.out.println(longestRepeatedSubstring("ababcaabcabcaab"));
	System.out.println(longestRepeatedSubstring("abababa"));
	System.out.println(longestRepeatedSubstring("abcabdfabababacbabacbdabdcabcdaaaabbbcccddabcdabcdabcdabababababacacacacacacdbdbdbdbdbdbdcabababacbababababacbdbacbddbcacbcbcdbdbdcabababa"));
	System.out.println(longestRepeatedSubstring("ababcaabcabcaabababcaabcabcaabababcaabcabcaabababcaabcabcaab"));
	end = System.nanoTime();
	
	System.out.println("time: " + (end-begin));
	
	System.out.println("Second approach using a suffix array from Coursera Princeton");
	begin = System.nanoTime();
	System.out.println(lrs2("banana"));
	System.out.println(lrs2("abaaabac"));
	System.out.println(lrs2("abacabac"));
	System.out.println(lrs2("ababcaabcabcaab"));
	System.out.println(lrs2("abababa"));
	System.out.println(lrs2("abcabdfabababacbabacbdabdcabcdaaaabbbcccddabcdabcdabcdabababababacacacacacacdbdbdbdbdbdbdcabababacbababababacbdbacbddbcacbcbcdbdbdcabababa"));
	System.out.println(lrs2("ababcaabcabcaabababcaabcabcaabababcaabcabcaabababcaabcabcaab"));
	end = System.nanoTime();
	
	System.out.println("time: " + (end-begin));
	
	System.out.println("Third approach using a suffix array with a wrapper object to use less memory from Coursera Princeton");
	begin = System.nanoTime();
	System.out.println(lrs3("banana"));
	System.out.println(lrs3("abaaabac"));
	System.out.println(lrs3("abacabac"));
	System.out.println(lrs3("ababcaabcabcaab"));
	System.out.println(lrs3("abababa"));
	System.out.println(lrs3("abcabdfabababacbabacbdabdcabcdaaaabbbcccddabcdabcdabcdabababababacacacacacacdbdbdbdbdbdbdcabababacbababababacbdbacbddbcacbcbcdbdbdcabababa"));
	System.out.println(lrs3("ababcaabcabcaabababcaabcabcaabababcaabcabcaabababcaabcabcaab"));
	end = System.nanoTime();
	
	System.out.println("time: " + (end-begin));
	
	System.out.println("\n  i ind lcp rnk select");
	System.out.println("---------------------------");
	
	String s = "ababcaabcabcaab";
	Suffix[] suffixes = createSuffixArray(s);
	
	for (int i = 0; i < s.length()-1; i++) {
            int index = suffixes[i].index;
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            assert s.substring(index).equals(suffixes[i].substring(index));
            int rank = rank(s.substring(index), suffixes);
            if (i == 0) {
        	System.out.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
            }
            else {
                int lcp = lcpSuffix(suffixes[i], suffixes[i+1]);
                System.out.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
            }
        }
	
	// random test using binary search for an element not present
	s= "abc";
	System.out.print("random test using binary search for an element not present: " + s + " " + rank(s, suffixes));
    }

}
