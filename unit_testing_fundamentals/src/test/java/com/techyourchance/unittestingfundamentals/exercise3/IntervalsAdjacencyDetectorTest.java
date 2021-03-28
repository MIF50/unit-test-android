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
    public void isAdjacent_whenInterval1BeforeInternal2_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(6,13);

        boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1AfterInternal2_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-5,-2);

        boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1ContainsInterval2_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(0,4);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1ContainsWithInterval2_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-2,6);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1OverlapInterval2OnStart_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-3,4);


        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1OverlapInterval2OnEnd_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(3,12);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenInterval1TheSameInterval2_shouldReturnFalse() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-1,5);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_whenIntervalIsAfterAdjacentInterval2_shouldReturnTrue() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(-5,-1);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_whenIntervalIsBeforeAdjacentInterval2_shouldReturnTrue() {
        Interval interval1 = new Interval(-1,5);
        Interval interval2 = new Interval(5,8);

        boolean result = SUT.isAdjacent(interval1, interval2);
        assertThat(result, is(true));
    }
}