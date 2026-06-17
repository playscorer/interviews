package citi.prep2026;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

record Order(String id) {}

public class ProducerConsumer {

    static final Order POISON_PILL = new Order("SHUTDOWN");

    static class Producer implements Runnable {
        private final BlockingQueue<Order> queue;
        private final int orderCount;

        Producer(BlockingQueue<Order> queue, int orderCount) {
            this.queue = queue;
            this.orderCount = orderCount;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < orderCount; i++) {
                    Order order = new Order("ORDER-" + i);
                    queue.put(order);
                    System.out.println(Thread.currentThread().getName()
                            + " produced: " + order.id());
                }
                queue.put(POISON_PILL);
                System.out.println(Thread.currentThread().getName()
                        + " sent poison pill");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Order> queue;

        Consumer(BlockingQueue<Order> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Order order = queue.take();
                    if (order == POISON_PILL) {
                        System.out.println(Thread.currentThread().getName()
                                + " received poison pill — exiting");
                        queue.put(POISON_PILL); // pass to next consumer
                        return;
                    }
                    process(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        void process(Order order) {
            System.out.println(Thread.currentThread().getName()
                    + " processed: " + order.id());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(100);
        int consumerCount = 10;

        ExecutorService producerPool = Executors.newSingleThreadExecutor(
                r -> {
                    Thread t = new Thread(r);
                    t.setName("producer-thread");
                    return t;
                }
        );
        ExecutorService consumerPool = Executors.newFixedThreadPool(
                consumerCount,
                new ThreadFactory() {
                    private final AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("consumer-thread-" + count.incrementAndGet());
                        return t;
                    }
                }
        );

        producerPool.submit(new Producer(queue, 500));

        for (int i = 0; i < consumerCount; i++) {
            consumerPool.submit(new Consumer(queue));
        }

        producerPool.shutdown();
        producerPool.awaitTermination(60, TimeUnit.SECONDS);

        consumerPool.shutdown();
        consumerPool.awaitTermination(60, TimeUnit.SECONDS);
    }
}