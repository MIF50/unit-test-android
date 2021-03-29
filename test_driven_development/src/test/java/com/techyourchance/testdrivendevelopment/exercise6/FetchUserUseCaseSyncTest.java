package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTest {

    // region constants ----------------------------------------------------------------------------
    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final User USER = new User(USER_ID, USERNAME);
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private FetchUserHttpEndpointSyncTestDouble mFetchUserHttpEndpointSyncTestDouble;
    @Mock UsersCache mUsersCacheMock;
    // endregion helper fields ---------------------------------------------------------------------

    private FetchUserUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        mFetchUserHttpEndpointSyncTestDouble = new FetchUserHttpEndpointSyncTestDouble();
        SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSyncTestDouble, mUsersCacheMock);
        userNotInCache();
        success();
    }

    @Test
    public void fetchUserSync_whenNotInCache_shouldPassedCorrectUserIdToEndpoint() throws Exception {
        // Arrange
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(mFetchUserHttpEndpointSyncTestDouble.mUserId, is(USER_ID));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointSuccess_shouldReturnSuccess() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointSuccess_shouldReturnCorrectUser() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointSuccess_shouldUserCached() throws Exception {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).cacheUser(ac.capture());
        assertThat(ac.getValue(), is(USER));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointAuthError_shouldReturnFailure() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointAuthError_shouldReturnNullUser() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), nullValue());
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointAuthError_shouldReturnNothingCached() throws Exception {
        // Arrange
        authError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointServerError_shouldReturnFailure() throws Exception {
        // Arrange
        serverError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.FAILURE));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointServerError_shouldReturnNullUser() throws Exception {
        // Arrange
        serverError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointServerError_shouldReturnNothingCached() throws Exception {
        // Arrange
        serverError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointNetworkError_shouldReturnNetworkError() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.NETWORK_ERROR));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointNetworkError_shouldReturnNullUser() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(nullValue()));
    }

    @Test
    public void fetchUserSync_whenNotInCacheEndpointNetworkError_shouldReturnNothingCached() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_whenInCache_shouldPassedCorrectUserIdToCache() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).getUser(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUserSync_whenInCache_shouldReturnSuccess() throws Exception {
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(FetchUserUseCaseSync.Status.SUCCESS));
    }

    @Test
    public void fetchUserSync_whenInCache_shouldReturnCachedUser() throws Exception {
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUserSync_whenInCache_shouldReturnEndpointNotPolled() throws Exception {
        // Arrange
        userInCache();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(mFetchUserHttpEndpointSyncTestDouble.mRequestCount, is(0));
    }

    // region helper methods -----------------------------------------------------------------------
    private void userNotInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(null);
    }

    private void userInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    private void success() {
        // endpoint test double is set up for success by default; this method is for clarity of intent
    }

    private void authError() {
        mFetchUserHttpEndpointSyncTestDouble.mAuthError = true;
    }

    private void serverError() {
        mFetchUserHttpEndpointSyncTestDouble.mServerError = true;
    }

    private void networkError() { mFetchUserHttpEndpointSyncTestDouble.mNetworkError = true;}
    // endregion helper methods --------------------------------------------------------------------


    // region helper classes -----------------------------------------------------------------------
    static class FetchUserHttpEndpointSyncTestDouble implements FetchUserHttpEndpointSync {

        private int mRequestCount;
        private String mUserId = "";
        private boolean mAuthError;
        private boolean mServerError;
        private boolean mNetworkError;

        @Override
        public EndpointResult fetchUserSync(String userId) throws NetworkErrorException {
            mRequestCount ++;
            mUserId = userId;

            if (mAuthError) {
                return new EndpointResult(EndpointStatus.AUTH_ERROR, "", "");
            } else if (mServerError) {
                return new EndpointResult(EndpointStatus.GENERAL_ERROR, "", "");
            } else if (mNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USERNAME);
            }
        }
    }
    // endregion helper classes --------------------------------------------------------------------

}