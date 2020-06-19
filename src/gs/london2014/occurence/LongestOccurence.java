package gs.london2014.occurence;

public class LongestOccurence {

    static int count(String str) {
	int lgest = 1;
	int lg = 1;

	for (int i = 0; i < str.length() - 1; i++) {
	    if (str.charAt(i) == str.charAt(i + 1)) {
		lg++;
	    } else {
		if (lgest < lg)
		    lgest = lg;
		lg = 1;
	    }
	}

	if (lgest < lg)
	    lgest = lg;

	return lgest;
    }

    static String longest(String str) {
	int lgest = 1;
	int lg = 1;
	StringBuilder current = new StringBuilder();
	current.append(str.charAt(0));
	String longest = null;

	for (int i = 0; i < str.length() - 1; i++) {
	    if (str.charAt(i) == str.charAt(i + 1)) {
		lg++;
		current.append(str.charAt(i + 1));
	    } else {
		if (lgest < lg) {
		    lgest = lg;
		    longest = current.toString();
		}
		lg = 1;
		current = new StringBuilder();
		current.append(str.charAt(i + 1));
	    }
	}

	if (lgest < lg) {
	    lgest = lg;
	    longest = current.toString();
	}

	return longest;
    }

    public static void main(String[] args) {
	String str = "eeeerttttt";
	System.out.println("longest : " + count(str));
	System.out.println("longest : " + longest(str));
    }

}
