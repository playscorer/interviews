package multithreading.producer_consumer;

import java.util.List;
import java.util.Random;

public class Producer implements Runnable {
    
    private Random randomizer = new Random();
    
    private List<Integer> queue;

    private int capacity;
    
    public Producer(List<Integer> queue, int capacity) {
	this.queue = queue;
	this.capacity = capacity;
    }

    @Override
    public void run() {
	
	synchronized(queue) {
	    while (true) {
		if (queue.size() == capacity) {
		    try {
			queue.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		while (queue.size() < capacity) {
		    int nextInt = randomizer.nextInt(500);
		    System.out.println("Producer #" + Thread.currentThread().getName() + " : " + nextInt);
		    queue.add(nextInt);
		    queue.notifyAll();
		}
	    }
	}
    }
    
    

}
