package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setup(){
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void isNegative_whenNegativeNumber_shouldReturnTrue(){
        boolean result = SUT.isNegative(-10);
        assertThat(result, is(true));
    }

    @Test
    public void isNegative_whenZero_shouldReturnFalse(){
        boolean result = SUT.isNegative(0);
        assertThat(result, is(false));
    }
    @Test
    public void isNegative_whenPositiveNumber_shouldReturnFalse(){
        boolean result = SUT.isNegative(1);
        assertThat(result, is(false));
    }

}