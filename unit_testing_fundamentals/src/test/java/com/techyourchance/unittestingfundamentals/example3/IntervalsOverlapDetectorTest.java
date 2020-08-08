package com.techyourchance.unittestingfundamentals.example3;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntervalsOverlapDetectorTest {

    IntervalsOverlapDetector SUT;

    @Before
    public void setup() throws Exception {
        SUT = new IntervalsOverlapDetector();
    }

    // interval1 is before interval2
    @Test
    public void isOverlap_givenInterval1BeforeInterval2_returnFalse() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(8, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 overlaps interval2 on start
    @Test
    public void isOverlap_givenInterval1OverlapsInterval2OnStart_returnTrue() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(3, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 is contained within interval2
    @Test
    public void isOverlap_givenInterval1ContainedWithinInterval2_returnTrue() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-4, 12);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 contains interval2
    @Test
    public void isOverlap_givenInterval1ContainsInterval2_returnTrue() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(0, 3);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 overlaps interval2 on end
    @Test
    public void isOverlap_givenInterval1OverlapsInterval2OnEnd_returnTrue() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-4, 4);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(true));
    }

    // interval1 is after interval2
    @Test
    public void isOverlap_givenInterval1AfterInterval2_returnFalse() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-10, -3);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 is before and adjacent interval2
    @Test
    public void isOverlap_givenInterval1BeforeAdjacentInterval2_returnFalse() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(5, 8);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }

    // interval1 is after and adjacent interval 2
    @Test
    public void isOverlap_interval1AfterAdjacentInterval2_returnFalse() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-3, -1);
        boolean result = SUT.isOverlap(interval1, interval2);
        assertThat(result, is(false));
    }
}