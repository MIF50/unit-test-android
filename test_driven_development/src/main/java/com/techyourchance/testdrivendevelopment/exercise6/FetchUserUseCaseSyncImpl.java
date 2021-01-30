package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private final FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private final UsersCache mUsersCache;


    public FetchUserUseCaseSyncImpl(
            FetchUserHttpEndpointSync mFetchUserHttpEndpointSync,
            UsersCache mUsersCache
    ) {
        this.mFetchUserHttpEndpointSync = mFetchUserHttpEndpointSync;
        this.mUsersCache = mUsersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        EndpointResult fetchUser;

        if (mUsersCache.getUser(userId) != null) {
            return new UseCaseResult(FetchUserUseCaseSync.Status.SUCCESS, mUsersCache.getUser(userId));
        } else {
            try {
                fetchUser = mFetchUserHttpEndpointSync.fetchUserSync(userId);
            } catch (NetworkErrorException e) {
                return new UseCaseResult(Status.NETWORK_ERROR, null);
            }

            switch (fetchUser.getStatus()) {
                case SUCCESS:
                    User user = new User(fetchUser.getUserId(), fetchUser.getUsername());
                    mUsersCache.cacheUser(user);
                    return new UseCaseResult(FetchUserUseCaseSync.Status.SUCCESS, user);
                case AUTH_ERROR:
                case GENERAL_ERROR:
                    return new UseCaseResult(Status.FAILURE, null);
                default:
                    throw new RuntimeException("invalid status" + fetchUser.getStatus());

            }
        }
    }
}
