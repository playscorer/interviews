package java8.functional_interface;

public class TestFunc {

    public static void main(String[] args) {
	TestObj t = new TestObj("test");
	MyFunc f  = s -> s + ".suffix";
	t.add(f);
	System.out.println(t.getX());
    }

}
