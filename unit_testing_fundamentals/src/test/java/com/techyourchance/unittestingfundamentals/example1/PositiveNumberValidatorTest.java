package com.techyourchance.unittestingfundamentals.example1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PositiveNumberValidatorTest {

    PositiveNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new PositiveNumberValidator();
    }

    @Test
    public void isPositive_whenPositiveNumber_shouldReturnTrue() {
        boolean result = SUT.isPositive(1);
        assertThat(result, is(true));
    }

    @Test
    public void isPositive_whenZero_shouldReturnFalse() {
        boolean result = SUT.isPositive(0);
        assertThat(result, is(false));
    }

    @Test
    public void isPositive_whenNegativeNumber_shouldReturnFalse() {
        boolean result = SUT.isPositive(-1);
        assertThat(result, is(false));
    }
}