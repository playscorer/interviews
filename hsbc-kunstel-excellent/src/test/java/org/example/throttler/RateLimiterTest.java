package org.example.throttler;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    void testSimpleBoundary() throws Exception {
        var sut = getSut();

        var list = new ArrayList<ThrottleUnit>();
        for (int ii=0; ii<10; ++ii) {
            var tu = sut.tryTake();
            assertNotNull(tu);
            list.add(tu);
        }
        // any further will fail
        assertNull(sut.tryTake());
        list.forEach(v-> {
            try {
                v.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }); // all have an end timestamp at 1000
        list.clear();

        for (int ii=0; ii<20; ++ii) {
            var tu = sut.tryTake();
            list.add(tu);
            if (tu != null) {
                tu.close();
            }
            sut.setTime(sut.getTime()+1);
        }
        for (int ii=0; ii<10; ++ii) {
            assertNull(list.get(ii)); // first 10 tries will not succeed
        }
        for (int ii=10; ii<20; ++ii) {
            assertNull(list.get(ii)); // next 10 tries will succeed
        }
        var tu = list.get(10);

        // we have 10 added earlier at 1000 with a 10ms time window - maxing the rate limit
        // which means the first chance to get another timeslice is at 1010
        assertEquals(tu.getEndTime(), 1010);
        assertEquals(tu.getStartTime(), 1010);
        assertEquals(tu.getEndTime(), 1010);

        assertNull(list.get(9));
    }

    // test long chain of events right at the edge of allowable
    @Test
    void testStreamThrottle() throws Exception {
        var sut = getSut();

        var list = new ArrayList<ThrottleUnit>();
        for (int ii=0; ii<100; ++ii) {
            var tu = sut.tryTake();
            assertNotNull(tu); // it should succeed - we are right on the edge of allowable
            list.add(tu);
            tu.close();
            sut.setTime(sut.getTime()+1);
        }
    }

    // writing at twice the top throttle rate, we should see pause behaviour
    @Test
    void testStreamOverThrottle() throws Exception {
        var sut = getSut();

        var list = new ArrayList<ThrottleUnit>();
        for (int ii=0; ii<100; ++ii) {
            for (int jj=0; jj<2; ++jj) {
                var tu = sut.tryTake();
                list.add(tu);
                if (tu!=null) {
                    tu.close();
                }
            }
            sut.setTime(sut.getTime()+1);
        }
        String finalText = String.join(",", list.stream().map(v -> v==null ? "null": ""+v.getStartTime()).toArray(String[]::new));

        // ensure we have null where we should expect failures, and print the timestamps of where we succeeded
        assertEquals(finalText,"1000,1000,1001,1001,1002,1002,1003,1003,1004,1004," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1010,1010,1011,1011,1012,1012,1013,1013,1014,1014," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1020,1020,1021,1021,1022,1022,1023,1023,1024,1024," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1030,1030,1031,1031,1032,1032,1033,1033,1034,1034," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1040,1040,1041,1041,1042,1042,1043,1043,1044,1044," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1050,1050,1051,1051,1052,1052,1053,1053,1054,1054," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1060,1060,1061,1061,1062,1062,1063,1063,1064,1064," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1070,1070,1071,1071,1072,1072,1073,1073,1074,1074," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1080,1080,1081,1081,1082,1082,1083,1083,1084,1084," +
                "null,null,null,null,null,null,null,null,null,null," +
                "1090,1090,1091,1091,1092,1092,1093,1093,1094,1094," +
                "null,null,null,null,null,null,null,null,null,null");
    }

    @Test
    void testCompletableFutures() {
        try (var sut = getSut()) {

            var list = new ArrayList<ThrottleUnit>();
            // make large blast at start time - these should all succeed and return the value immediately
            for (int ii=0; ii<10; ++ii) {
                CompletableFuture<ThrottleUnit> cf = sut.takeNext();
                assertNotNull(cf);
                assertTrue(cf.isDone());
                assertTrue(!cf.isCompletedExceptionally());
                var tu = cf.get();
                assertNotNull(tu);
                tu.close();
            }
            // ensure next try attempt will fail
            var tu = sut.tryTake();
            assertNull(tu);

            // therefore, the next future request will return an non-complete future
            CompletableFuture<ThrottleUnit> cf = sut.takeNext();
            assertNotNull(cf);
            assertTrue(!cf.isDone());
            assertTrue(!cf.isCompletedExceptionally());

            sut.setTime(1009);
            sut.houseKeep();
            assertNotNull(cf);
            assertTrue(!cf.isDone());
            assertTrue(!cf.isCompletedExceptionally());

            // as we clock over the time boundary, our pending CompletableFuture should now be done
            sut.setTime(1010);

            assertNotNull(cf);
            assertTrue(cf.isDone());
            assertTrue(!cf.isCompletedExceptionally());

            var ret = cf.get();
            assertNotNull(ret);
            assertEquals(ret.getStartTime(), 1010);
            assertNotSame(ret.getEndTime(), 1010); // end time wont be the same as we have not done anything with it

            // we "do something" for 5ms, then close the ThrottleUnit
            sut.setTime(1015);
            sut.houseKeep();
            ret.close();
            sut.setTime(1016);
            sut.houseKeep();
            // the end time of the task was at the close time
            assertEquals(ret.getEndTime(), 1015);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RateLimiterSub getSut() {
        var sut = new RateLimiterSub(10, 10);
        sut.setTime(1000);
        return sut;
    }

    public static class RateLimiterSub extends RateLimiter {
        private AtomicLong time = new AtomicLong();

        public RateLimiterSub(int maxItems, long msTimePeriod) {
            super(maxItems, msTimePeriod);
        }

        protected long getCurrentTimeMs() {
            return time.get();
        }

        public long getTime() {
            return time.get();
        }

        public synchronized void setTime(long time) {
            this.time.set(time);
            houseKeep();
            this.notifyAll();
        }

        public void houseKeep() {
            var toProcess = new TreeMap<ThrottleUnit, CompletableFuture<ThrottleUnit>>();
            houseKeeping(toProcess);
        }
    }
}
