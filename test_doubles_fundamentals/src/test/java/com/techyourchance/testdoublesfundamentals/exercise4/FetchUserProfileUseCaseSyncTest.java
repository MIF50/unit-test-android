package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class FetchUserProfileUseCaseSyncTest {


    private static final String USER_ID = "user_id";
    private static final String FULL_NAME = "fullName";
    private static final String IMAGE_URL = "imageUrl";

    private UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncId;
    private UsersCacheId mUserCachedId;

    private FetchUserProfileUseCaseSync SUT;


    @Before
    public void setUp() throws Exception {
        mUserProfileHttpEndpointSyncId = new UserProfileHttpEndpointSyncTd();
        mUserCachedId = new UsersCacheId();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncId,mUserCachedId);

    }

    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndPoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserProfileHttpEndpointSyncId.mUserId,is(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_success_userCached() {
        SUT.fetchUserProfileSync(USER_ID);
        User cachedUser = mUserCachedId.getUser(USER_ID);
        assertThat(cachedUser.getUserId(), is(USER_ID));
        assertThat(cachedUser.getFullName(), is(FULL_NAME));
        assertThat(cachedUser.getUserId(), is(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_generalError_userNotCached() {
        mUserProfileHttpEndpointSyncId.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserCachedId.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_authError_userNotCached() {
        mUserProfileHttpEndpointSyncId.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserCachedId.getUser(USER_ID), is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_serverError_userNotCached() {
        mUserProfileHttpEndpointSyncId.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(mUserCachedId.getUser(USER_ID),is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_success_returnSuccess() {
      UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
      assertThat(result,is(UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchUserProfileSync_serverError_returnFailure() {
        mUserProfileHttpEndpointSyncId.mIsServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result,is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_authError_returnFailure() {
        mUserProfileHttpEndpointSyncId.mIsAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result,is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_generalError_returnFailure() {
        mUserProfileHttpEndpointSyncId.mIsGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result,is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUserProfileSync_networkError_returnedNetworkError() {
        mUserProfileHttpEndpointSyncId.mIsNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(result,is(UseCaseResult.NETWORK_ERROR));
    }




    //---------------------------------------------------------------------------------------------------------
    // Helper classes
    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {

        private String mUserId = "";

        private boolean mIsGeneralError;
        private boolean mIsAuthError;
        private boolean mIsServerError;
        private boolean mIsNetworkError;


        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;

            if (mIsGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"","","");
            } else if (mIsAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR,"","","");
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR,"","","");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS,USER_ID,FULL_NAME,IMAGE_URL);
            }

        }
    }

    private static class UsersCacheId implements UsersCache {
        List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user: mUsers) {
                if (user.getUserId().equals(userId)){
                    return user;
                }
            }
            return null;
        }
    }
}