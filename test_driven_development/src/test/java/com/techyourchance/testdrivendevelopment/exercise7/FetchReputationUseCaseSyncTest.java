package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR;
import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus.SUCCESS;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentCaptor.*;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    // region constants -------------------------------------------------------------
    private final int REPUTATION = 1;

    // end region constants ---------------------------------------------------------

    // region helper fields ---------------------------------------------------------
    @Mock
    GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;
    // end region helper fields ----------------------------------------------------------


    FetchReputationUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchReputationUseCaseSync(mGetReputationHttpEndpointSyncMock);
        success();
    }

    @Test
    public void fetchReputation_success_returnSuccess()throws Exception {
        // Arrange
        // Act
        SUT.fetchReputation();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation();
        // Assert
        assertThat(result,is(FetchReputationUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchReputation_success_returnReputationSuccess() {
        // Arrange
        // Act
        SUT.fetchReputation();
        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSyncMock.getReputationSync();
        // Assert
        assertThat(result.getReputation(),is(REPUTATION));

    }

    @Test
    public void fetchReputation_failure_returnFailure() {
        // Arrange
        authError();
        // Act
        SUT.fetchReputation();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation();
        // Assert
        assertThat(result,is(FetchReputationUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchReputation_failure_returnReputationZeroFailure() {
        // Arrange
        authError();
        // Act
        SUT.fetchReputation();
        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSyncMock.getReputationSync();
        // Assert
        assertThat(result.getReputation(),is(0));
    }

    // region helper methods ---------------------------------------------------------------------------------------------------
    private void success() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(new EndpointResult(SUCCESS,REPUTATION));
    }

    private void authError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync()).thenReturn(new EndpointResult(GENERAL_ERROR,0));
    }
    // end region helper methods  ------------------------------------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------------------------------------

    // end region helper classes ----------------------------------------------------------------------------------------------------


}