package com.techyourchance.testdoublesfundamentals.example5;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserInputValidatorTest {

    UserInputValidator SUT;

    @Before
    public void setup() throws Exception {
        SUT = new UserInputValidator();
    }

    @Test
    public void isValidFullName_whenValidFullName_shouldReturnTrue() throws Exception {
        boolean result = SUT.isValidFullName("validFullName");
        assertThat(result, is(true));
    }

    @Test
    public void isValidFullName_whenInvalidFullName_shouldReturnFalse() throws Exception {
        boolean result = SUT.isValidFullName("");
        assertThat(result, is(false));
    }

    @Test
    public void isValidUsername_whenValidUsername_shouldReturnTrue() throws Exception {
        boolean result = SUT.isValidUsername("validUsername");
        assertThat(result, is(true));
    }

    @Test
    public void isValidUsername_whenInvalidUsername_shouldReturnFalse() throws Exception {
        boolean result = SUT.isValidUsername("");
        assertThat(result, is(false));
    }
}