package org.example.eventbus;

import org.example.eventbus.consumer.Subscription;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MultiEventBusTest {

    protected MultiEventBus<String> getSut() {
        return new MultiEventBus<String>(20);
    }

    // register and test simple callback
    @Test
    void testRegisterSimple() {
        var sut = getSut();
        AtomicLong cb = new AtomicLong(-1);
        AtomicReference ret = new AtomicReference();
        var subscription = sut.register((data, seqId) -> {
           ret.set(data);
           cb.set(seqId);
        });

        assertNotNull(subscription);
        assertEquals(1L, subscription.getSubscriptionId());

        sut.publishEvent("Hello World");
        sut.pump();

        // ensure the callback was called
        assertEquals(cb.get(), 0);
        assertEquals(ret.get(), "Hello World");

        assertTrue(sut.unregister(subscription.getSubscriptionId()));
        assertFalse(sut.unregister(subscription.getSubscriptionId()));
    }

    // Test register and ensure that event matcher functionality is also working
    @Test
    void testRegisterFull() {
        var sut = getSut();
        AtomicLong cb = new AtomicLong(-1);
        AtomicReference ret = new AtomicReference<>();

        Subscription<String> subscription = new Subscription<>();
        subscription.setCallback((data, seqId) -> {
            ret.set(data);
            cb.set(seqId);
        });
        // test matcher to only allow certain events to be called back
        subscription.setMatcher((str) -> str.endsWith(" 2"));

        sut.register(subscription);

        assertNotNull(subscription);
        assertEquals(1L, subscription.getSubscriptionId());

        pushEvents(sut, 10);
        while (sut.pump()) {} // ensure all events are passed to callbacks if possible

        // ensure the callback was called
        assertEquals(cb.get(), 2);
        assertEquals(ret.get(), "Hello World");

        assertTrue(sut.unregister(subscription.getSubscriptionId()));
        assertFalse(sut.unregister(subscription.getSubscriptionId()));
    }

    private void pushEvents(MultiEventBus<String> sut, int count) {
        for (int i1=0; i1<count; ++i1) {
            sut.publishEvent("Hello World " + i1);
        }
    }

    private void pushEventsYield(MultiEventBus<String> sut, int count, int writeDelay) {
        for (int i1=0; i1<count; ++i1) {
            sut.publishEvent("Hello World " + i1);
            try {
                if (writeDelay>0) {
                    Thread.sleep(writeDelay);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    // Test simple case of multiple consumers/subscribers
    @Test
    void testMultiConsumers() {
        var sut = getSut();
        List<String> list = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        Subscription<String> subscription = new Subscription<>();
        subscription.setCallback((data, seqId) -> {
            list2.add(data + ":" + seqId); // callback adds to our list
        });
        // test matcher to only allow certain events to be called back
        subscription.setMatcher((str) -> str.endsWith(" 2"));
        sut.register(subscription);

        // create second consumer
        sut.register((d, seqId) -> list.add(d + ":" + seqId));

        assertNotNull(subscription);
        assertEquals(1L, subscription.getSubscriptionId());

        // push 10 events, first consumer should get 1 (filtered), the second 10
        pushEvents(sut, 10);
        while (sut.pump()) {} // ensure all events are passed to callbacks if possible

        // first consumer is filtered
        // ensure the callback was called
        assertEquals(list2.size(), 1);
        assertEquals(list2.get(0), "Hello World 2:2");

        assertEquals(list.size(), 10);
        String commaSeparatedString = String.join(",", list);
        assertEquals(commaSeparatedString, "Hello world 0:0,Hello world 1:1,Hello world 2:2,Hello world 3:3,Hello world 4:4,Hello world 5:5,Hello world 6:6,Hello world 7:7,Hello world 8:8,Hello world 9:9");

        assertTrue(sut.unregister(subscription.getSubscriptionId()));
        assertFalse(sut.unregister(subscription.getSubscriptionId()));
    }

    // test case of overfillinf/data loss caused when backpressure is off
    // on pumping data, we expect an initial error to be returned that we have missed data
    @Test
    void testOverFill() {
        var sut = getSut();
        sut.setProvideSimpleBackPressure(false);

        List<String> list = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();

        Subscription<String> subscription = new Subscription<>();
        subscription.setCallback((data, seqId) -> {
            list.add(data + ":" + seqId); // callback adds to our list
        });
        AtomicReference s = new AtomicReference<Subscription>();

        subscription.setOnException(((ex, sub) -> {
            exceptions.add(ex);
            s.set(sub);
        }));

        // test matcher to only allow certain events to be called back
        sut.register(subscription);

        // we push too many events into the ring buffer, so we will lose data as we are not pumping enough
        pushEvents(sut, 100);
        while (sut.pump()) {} // ensure all events are passed to callbacks if possible

        // first consumer is filtered
        // ensure the callback was called
        assertEquals(exceptions.size(), 1);
        assertEquals(s.get(), "Hello World 2:2");

        String ex = exceptions.get(0).toString();
        assertEquals(ex,"java.lang.IllegalStateException: Could not keep up with writer, lost 82 packets");

        // however we still received the remaining data
        assertEquals(list.size(), 10);
    }

    // test with backpressure turned off - as we have a vsmall ringbuffer, we rely on 0.5ms delay between writes
    @Test
    void testCreateWorkerThreadsThreadsSlowWriters() throws InterruptedException {
        testMultiConsumerProducers(1, false); // slow writers so no backpressure needed
    }

    // fast test, 2 writer threads, multiple consumer threads, backpressure on
    @Test
    void testCreateWorkerThreadsThreadsFastWriters() throws InterruptedException {
        testMultiConsumerProducers(0, true); // fast writers so backpressure needed
    }

    private void testMultiConsumerProducers(int writeDelay, boolean backPressure) throws InterruptedException {
        var sut = getSut();
        sut.setProvideSimpleBackPressure(backPressure);
        int sendCount = 0;

        List<String> list = new ArrayList<>();
        var sub1 = sut.register((data, seqId) -> {
            list.add(data + ":" + seqId); // callback adds to our list
        });
        List<String> list2 = new ArrayList<>();
        sut.register((data, seqId) -> {
            list2.add(data + ":" + seqId); // callback adds to our list
        });
        List<String> list3 = new ArrayList<>();
        sut.register((data, seqId) -> {
            list3.add(data + ":" + seqId); // callback adds to our list
        });

        // threads will immediately start processing data
        var cancellation = sut.createWorkerThreads(4);

        Thread t1 = new Thread(() -> pushEventsYield(sut, 1000, writeDelay));
        Thread t2 = new Thread(() -> pushEventsYield(sut, 1000, writeDelay));
        t1.start();
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
        }

        // at this point, we have finished writing, so lets ensure we have completed reading
        cancellation.apply(false); //soft cancel, wait for curent processing to end

        // ensure our worker threads successfully added to our lists
        // (callbaks are thread safe)
        assertEquals(list.size(), 2000);
        assertEquals(list2.size(), 2000);
        assertEquals(list3.size(), 2000);

        long readSeqNum = sub1.getReadSequenceNumber().get();
        assertEquals(readSeqNum, 2000);
    }
}
