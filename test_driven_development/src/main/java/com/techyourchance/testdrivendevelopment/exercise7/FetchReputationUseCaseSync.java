package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import static com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;

public class FetchReputationUseCaseSync {
    private GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock) {
        mGetReputationHttpEndpointSyncMock = getReputationHttpEndpointSyncMock;
    }

    public UseCaseResult fetchReputation() {
        EndpointResult result;
        result = mGetReputationHttpEndpointSyncMock.getReputationSync();
        switch (result.getStatus()) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("invalid result" + result);
        }
    }

    public enum UseCaseResult {
        SUCCESS,
        FAILURE
    }
}
