package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
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

    private FetchUserProfileUseCaseSync SUT;
    private UserProfileHttpEndpointSyncTD userProfileHttpEndpointSyncTD;
    private UsersCacheTD userCacheTD;

    @Before
    public void setup(){
        userProfileHttpEndpointSyncTD = new UserProfileHttpEndpointSyncTD();
        userCacheTD = new UsersCacheTD();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTD,userCacheTD);
    }

    @Test
    public void fetchUserSync_whenSuccess_shouldPassUserIdToUserProfileHttpEndPoint(){
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndpointSyncTD.userId,is(USER_ID));
    }

    @Test
    public void fetchUserSync_whenSuccess_shouldCachedUser(){
        SUT.fetchUserProfileSync(USER_ID);
        User userCached = userCacheTD.getUser(USER_ID);
        assertThat(userCached.getUserId(),is(USER_ID));
        assertThat(userCached.getFullName(),is(FULL_NAME));
        assertThat(userCached.getImageUrl(),is(IMAGE_URL));
    }

    @Test
    public void fetchUserSync_whenSuccess_shouldReturnSuccess(){
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS,is(result));
    }

    @Test
    public void fetchUserSync_whenAuthError_shouldNotCachedUser() throws NetworkErrorException {
        userProfileHttpEndpointSyncTD.isAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        UserProfileHttpEndpointSync.EndpointResult result = userProfileHttpEndpointSyncTD.getUserProfile(USER_ID);
        assertThat(result.getUserId(),is(""));
        assertThat(result.getFullName(),is(""));
        assertThat(result.getImageUrl(),is(""));
    }

    @Test
    public void fetchUserSync_whenGeneralError_shouldNotCachedUser() throws NetworkErrorException {
        userProfileHttpEndpointSyncTD.isGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        UserProfileHttpEndpointSync.EndpointResult result = userProfileHttpEndpointSyncTD.getUserProfile(USER_ID);
        assertThat(result.getUserId(),is(""));
        assertThat(result.getFullName(),is(""));
        assertThat(result.getImageUrl(),is(""));
    }

    @Test
    public void fetchUserSync_whenServerError_shouldNotCachedUser() throws NetworkErrorException {
        userProfileHttpEndpointSyncTD.isServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        UserProfileHttpEndpointSync.EndpointResult result = userProfileHttpEndpointSyncTD.getUserProfile(USER_ID);
        assertThat(result.getUserId(),is(""));
        assertThat(result.getFullName(),is(""));
        assertThat(result.getImageUrl(),is(""));
    }

    @Test
    public void fetchUserSync_whenAuthError_shouldReturnFailure(){
        userProfileHttpEndpointSyncTD.isAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,is(result));
    }

    @Test
    public void fetchUserSync_whenGeneralError_shouldReturnFailure(){
        userProfileHttpEndpointSyncTD.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,is(result));
    }

    @Test
    public void fetchUserSync_whenServerError_shouldReturnFailure(){
        userProfileHttpEndpointSyncTD.isServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE,is(result));
    }

    @Test
    public void fetchUserSync_whenNetworkError_shouldReturnNetworkError(){
        userProfileHttpEndpointSyncTD.isNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertThat(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR,is(result));
    }

    // Helper Classes
    private static class UserProfileHttpEndpointSyncTD implements UserProfileHttpEndpointSync {

        private boolean isAuthError;
        private boolean isGeneralError;
        private boolean isServerError;
        private boolean isNetworkError;
        private String userId;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.userId = userId;
            if (isAuthError){
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR,"","","");
            } else if (isGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"","","");
            } else if (isServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR,"","","");
            } else if (isNetworkError){
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS,USER_ID,FULL_NAME,IMAGE_URL);
            }
        }
    }

    private static class UsersCacheTD implements UsersCache {

        private final List<User> users = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User exitUser = getUser(user.getUserId());
            if (exitUser != null) users.remove(exitUser);
            users.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user: users){
                if (user.getUserId().equals(userId)) return user;
            }
            return null;
        }
    }
}