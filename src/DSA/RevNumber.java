package DSA;

/**
 * Not Working
 * @author Filipe
 *
 */
public class RevNumber {
    
    public static int reverse(int n) {
	int res=0;
	
	while (n > 0) {
	    res = res << 1;
	    if ((int) (n & 1) == 1) {
		res = res ^ 1;
	    }
	    n = n >> 1;
	}
	return res;
    }
    
/*	int res=0;
	
	while (n > 0) {
	    res = res << 1;
	    res = res | ( n & 1);
	    n = n >> 1;
	}
	return res;*/

    public static void main(String[] args) {
	int rev = reverse(123);
	System.out.println(rev);
    }

}
