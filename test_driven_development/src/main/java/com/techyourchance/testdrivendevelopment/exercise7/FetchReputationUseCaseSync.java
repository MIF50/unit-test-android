package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

public class FetchReputationUseCaseSync {

    public enum UseCaseResult {
        FAILURE, SUCCESS
    }

    private final GetReputationHttpEndpointSync getReputationHttpEndpointSync;
    private final EventBus eventBus;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync, EventBus eventBus) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
        this.eventBus = eventBus;
    }

    public UseCaseResult fetchReputation() {
        GetReputationHttpEndpointSync.EndpointResult result =
                getReputationHttpEndpointSync.getReputationSync();
        switch (result.getStatus()) {
            case SUCCESS:
                eventBus.showReputation(result.getReputation());
                return UseCaseResult.SUCCESS;
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("invalid status " + result.getStatus());
        }

    }
}
