package DSA;

public class LongestPalindrome {
    
    public static String longestPalindrome(String s) {
        int length=0;
        String palindrome ="";
        
        for (int begin=0; begin<s.length()-1; begin++) {
            int end=s.length()-1;
            while (begin <= end) {
                String str = s.substring(begin, end+1);
                if (isPalindrome(s, begin, end) && length<str.length()) {
                    length=str.length();
                    palindrome=str;
                    break;
                }
                else {
                    end--;
                }
            }   
        }
        
        return palindrome;
    }
    
    private static boolean isPalindrome(String str, int begin, int end) {
        int start=begin;
        int finish=end;
        
        while (start<=finish && str.charAt(start) == str.charAt(finish)) {
            start++;
            finish--;
        } 
        
        return start>finish;
    }

    public static void main(String[] args) {
	String s = "babad";
	System.out.println(longestPalindrome(s));
    }

}
