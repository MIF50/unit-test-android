package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_whenEmptyString_shouldReturnEmptyString() {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }

    @Test
    public void duplicate_whenOneCharacter_shouldReturnTwoCharacter() {
        String result = SUT.duplicate("a");
        assertThat(result,is("aa"));

    }

    @Test
    public void duplicate_whenLongString_shouldReturnDuplicateLongString() {
        String result = SUT.duplicate("Mohamed Ibrahim");
        assertThat(result, is("Mohamed IbrahimMohamed Ibrahim"));
    }
}