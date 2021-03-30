package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    private final int REPUTATION = 1;
    private FetchReputationUseCaseSync SUT;

    @Mock
    private GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;
    @Mock
    private EventBus eventBusMock;

    @Before
    public void setup() {
        SUT = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock, eventBusMock);
        success();
    }

    @Test
    public void fetchReputation_whenSuccess_shouldReturnSuccess() {
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation();
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchReputation_whenSuccess_shouldReturnReputation() {
        ArgumentCaptor<Integer> ac = ArgumentCaptor.forClass(Integer.class);
        SUT.fetchReputation();
        verify(eventBusMock).showReputation(ac.capture());
        assertThat(ac.getValue(),is(REPUTATION));
    }

    @Test
    public void fetchReputation_whenGeneralError_shouldReturnFailure() {
        generalError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation();
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchReputation_whenGeneralError_shouldNotTrigger() {
        generalError();
        SUT.fetchReputation();
        verifyNoMoreInteractions(eventBusMock);
    }

    @Test
    public void fetchReputation_whenNetworkError_shouldReturnFailure() {
        networkError();
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation();
        assertThat(result, is(FetchReputationUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void fetchReputation_whenNetworkError_shouldNotTrigger() {
        networkError();
        SUT.fetchReputation();
        verifyNoMoreInteractions(eventBusMock);
    }

    // helper method
    public void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync
                        .EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION)
                );
    }

    public void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync
                        .EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0)
                );
    }

    public void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync
                        .EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0)
                );
    }
}