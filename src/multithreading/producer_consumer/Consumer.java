package multithreading.producer_consumer;

import java.util.List;

public class Consumer implements Runnable {
    
    private List<Integer> queue;

    public Consumer(List<Integer> queue) {
	this.queue = queue;
    }

    @Override
    public void run() {
	synchronized(queue) {
	    while (true) {
		if (queue.size() == 0) {
		    try {
			queue.wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
		while (queue.size() > 0) {
		    Integer i = queue.remove(0);
		    System.out.println("Consumer #" + Thread.currentThread().getName() + " : " + i);
		}
		queue.notifyAll();
	    }
	}
	
    }

}
