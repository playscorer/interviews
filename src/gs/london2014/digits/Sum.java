package gs.london2014.digits;

public class Sum {

    static int compute(int n) {
	if (n / 10 == 0)
	    return n;
	return n % 10 + compute(n / 10);
    }

    public static void main(String[] args) {
	System.out.println(compute(132));
    }

}
