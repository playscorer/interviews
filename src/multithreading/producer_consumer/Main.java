package multithreading.producer_consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    
    private static int CAPACITY = 100;
    private static int NB_CONSUMERS = 3;
    private static int NB_PRODUCERS = 5;
    
    public static void main(String[] args) {
	List<Integer> queue = new ArrayList<>();
	
	ExecutorService consumers = Executors.newFixedThreadPool(NB_CONSUMERS);
	for (int i=0; i<NB_CONSUMERS; i++) {
	    consumers.execute(new Consumer(queue));
	}
	
	ExecutorService producers = Executors.newFixedThreadPool(NB_PRODUCERS);
	for (int i=0; i<NB_PRODUCERS; i++) {
	    producers.execute(new Producer(queue, CAPACITY));
	}
    }

}
