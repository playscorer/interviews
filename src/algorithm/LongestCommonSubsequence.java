package algorithm;

/**
 * LCS for input Sequences “ABCDGH” and “AEDFHR” is “ADH” of length 3.
 * LCS for input Sequences “AGGTAB” and “GXTXAYB” is “GTAB” of length 4
 */
public class LongestCommonSubsequence {
    
    public static int lcsRecursive(String s, String t, int m, int n) {
	if (m == s.length() || n == t.length())
	    return 0;
	if (s.charAt(m) == t.charAt(n))
	    return 1 + lcsRecursive(s, t, m+1, n+1);
	else
	    return Math.max(lcsRecursive(s, t, m, n+1), lcsRecursive(s, t, m+1, n));
    }

    public static int lcs(String s, String t) {
	int m=s.length();
	int n=t.length();
	int[][] lcs = new int[m+1][n+1];
	
	for (int i=0; i<=m; i++) {
	    for (int j=0; j<=n; j++) {
		if (i==0 || j==0) {
		    lcs[i][j] = 0;
		} else if (s.charAt(i-1) == t.charAt(j-1)) {
		    lcs[i][j] = 1 + lcs[i-1][j-1];
		} else {
		    lcs[i][j] = Math.max(lcs[i-1][j], lcs[i][j-1]);
		}
	    }
	}
	
	/* print */
	int i=m;
	int j=n;
	StringBuilder builder = new StringBuilder();
	while (i>0 && j>0) {
	    // move up
	    if (lcs[i][j] == lcs[i-1][j]) {
		i--;
	    } 
	    // move left
	    else if (lcs[i][j] == lcs[i][j-1]) {
		j--;
	    } 
	    // move diagonale
	    else {
		builder.insert(0, s.charAt(i-1));
		i--;
		j--;
	    }
	}
	
	System.out.println(builder);
	
	return lcs[m][n];
    }
    
    
    public static void main(String[] args) {
	String s = "ABCDGH";
	String t = "AEDFHR";
	
	System.out.println("LongestCommonSubsequence (recursive) " + s + " " + t + " " + LongestCommonSubsequence.lcsRecursive(s, t, 0, 0));
	
	System.out.println("LongestCommonSubsequence " + s + " " + t + " " + LongestCommonSubsequence.lcs(s, t));
    }

}
