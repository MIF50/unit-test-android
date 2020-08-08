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
    public void duplicate_givenEmptyString_returnEmptyString() {
        String result = SUT.duplicate("");
        assertThat(result, is(""));
    }

    @Test
    public void duplicate_givenOneCharacter_returnTwoCharacter() {
        String result = SUT.duplicate("a");
        assertThat(result,is("aa"));

    }

    @Test
    public void duplicate_giveLongString_returnDuplicateLongString() {
        String result = SUT.duplicate("Mohamed Ibrahim");
        assertThat(result, is("Mohamed IbrahimMohamed Ibrahim"));
    }
}