package com.techyourchance.unittesting.questions;

import com.techyourchance.unittesting.common.time.TimeProvider;
import com.techyourchance.unittesting.networking.StackoverflowApi;
import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentCaptor.*;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class FetchQuestionDetailsUseCaseTest {

    // region constants ----------------------------------------------------

    // end region constants -------------------------------------------------


    // region helper fields -----------------------------------------------------
    @Mock FetchQuestionDetailsEndpoint mFetchQuestionDetailsEndpoint;
    @Mock
    TimeProvider mTimeProvider;

    // end region helper fields --------------------------------------------------


    FetchQuestionDetailsUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchQuestionDetailsUseCase(mFetchQuestionDetailsEndpoint,mTimeProvider);

    }

    // fetch question details correct question id passed to endpoint
    // fetch question details success observers notified with correct data
    // fetch question details unsubscribed not notified
    // fetch question details found in cache
    // fetch question details not found in cache
    //


    // region helper methods ---------------------------------------------------

    // end region helper methods -------------------------------------------------

    // region helper classes ------------------------------------------------------


    // end region helper classes ----------------------------------------------------


}