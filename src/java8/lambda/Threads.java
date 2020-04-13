package java8.lambda;

public class Threads {

    public static void main(String[] args) {
	Thread t = new Thread(() -> {
	    System.out.println("Hello From Another Thread");
	}); 
	t.start();
	
	Thread t2 = new Thread(new Runnable() {
	    @Override
	    public void run() {
		System.out.println("Hello From Another Thread 2");
	    }
	});
	t2.start();
    }

}
