package java8.functional_interface;

public class TestObj {
    
    private String x;
    
    public TestObj(String x) {
	this.x = x;
    }

    public void add(MyFunc f) {
	x = f.compute(x);
    }

    /**
     * @return the x
     */
    public String getX() {
        return x;
    }
    
}
