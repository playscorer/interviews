package org.example.statistics;

import org.example.statistics.ConditionalStatisticsCallback;
import org.example.statistics.LatencyStatistics;
import org.example.statistics.SlidingWindowStatistics;
import org.example.statistics.Statistics;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class LatencyStatisticsTest {

    public SlidingWindowStatistics getSut() {
        var sut = new LatencyStatistics(4);
        sut.add(1);
        sut.add(2);
        sut.add(3);
        sut.add(4);
        return sut;
    }

    @Test
    void testSubscribeForStatistics() {
        var sut = getSut();
        AtomicReference<Statistics> ret = new AtomicReference<>();
        sut.subscriberForStatistics((stat -> ret.set(stat)));
        assertNull(ret.get());
        sut.add(2);
        var value = ret.get();
        assertNotNull(value);
        assertEquals(value.getMean(), 2.75);
        assertEquals(value.getVariance(), 0.6875);
    }

    @Test
    void testSubscribeFilterStatistics() {
        var sut = getSut();
        List<Statistics> ret = new ArrayList<>();
        var cond = new ConditionalStatisticsCallback(
                (s) -> s.getMean() > 5.0, // condition
                v -> ret.add(v)
        ); // what to do if true

        sut.subscriberForStatistics(cond);

        for (int i1=0; i1<100; ++i1) {
            sut.add(i1);
        }

        assertEquals(ret.size(), 93);

        for (var p: ret) {
            assertTrue(p.getMean() > 5.0);
        }
    }

    @Test
    void testGetLatestStatistics() {
        var sut = getSut();
        var stats = sut.getLatestStatistics();
        assertEquals(stats.getMean(), 2.5);
        assertEquals(stats.getMean(), 1.25);

        sut = new LatencyStatistics(4);
        assertNull(sut.getLatestStatistics());
        sut.add(1);
        var out = sut.getLatestStatistics();
        assertEquals(out.getMean(), 1.0);
        assertEquals(out.getVariance(), 0.0);

        sut.add(2);
        out = sut.getLatestStatistics();
        assertEquals(out.getMean(), 1.5);
        assertEquals(out.getVariance(), 0.25);
    }
}
