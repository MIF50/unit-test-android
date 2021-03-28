package com.techyourchance.unittestingfundamentals.example2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringReverserTest {

    StringReverser SUT;

    @Before
    public void setup() throws Exception {
        SUT = new StringReverser();
    }

    @Test
    public void reverse_whenEmptyString_shouldReturnEmptyString() throws Exception {
        String result = SUT.reverse("");
        assertThat(result, is(""));
    }

    @Test
    public void reverse_whenSingleCharacter_shouldReturnSameString() throws Exception {
        String result = SUT.reverse("a");
        assertThat(result, is("a"));
    }

    @Test
    public void reverse_whenLongString_shouldReturnReversedString() throws Exception {
        String result = SUT.reverse("Vasiliy Zukanov");
        assertThat(result, is("vonakuZ yilisaV"));
    }
}