package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() {
        SUT = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_givenInterval1BeforeInternal2_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(6,13);

        boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1AfterInternal2_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-5,-2);

        boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1ContainsInterval2_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(0,4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1ContainsWithInterval2_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-2,6);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1OverlapInterval2OnStart_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-3,4);


        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1OverlapInterval2OnEnd_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(3,12);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenInterval1TheSameInterval2_returnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-1,5);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_givenIntervalIsAfterAdjacentInterval2_returnTrue() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-5,-1);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_givenIntervalIsBeforeAdjacentInterval2_returnTrue() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(5,8);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }
}