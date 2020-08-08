package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;


import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    private static final String USER_ID ="user_id";
    private static final String USER_NAME = "username";

    @Mock
    private UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;

    @Mock
    private UsersCache mUserCacheMock;

    @Mock
    private EventBusPoster mEventBusPosterMock;

    private UpdateUsernameUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUserCacheMock, mEventBusPosterMock);
        success();

    }

    @Test
    public void updateUsername_success_userIdAndUsernamePassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verify(mUpdateUsernameHttpEndpointSyncMock,times(1)).updateUsername(ac.capture(),ac.capture());

        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0),is(USER_ID));
        assertThat(captures.get(1),is(USER_NAME));
    }

    @Test
    public void updateUsername_success_userCached() throws Exception {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verify(mUserCacheMock).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertThat(cachedUser.getUserId(),is(USER_ID));
        assertThat(cachedUser.getUsername(), is(USER_NAME));

    }


    @Test
    public void updateUsername_generalError_userNotCached()throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsername_authError_userNotCached() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsername_serverError_userNotCached() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsername_success_triggerEventPoster() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(),is(instanceOf(UserDetailsChangedEvent.class)));

    }

    @Test
    public void updateUsername_generalError_noInteractionWithEventPosted()throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_authError_noInteractionWithEventPosted()throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);

    }

    @Test
    public void updateUsername_serverError_noInteractionWithEventPosted() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_success_returnSuccess() {
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUsername_serverError_returnFailure()throws Exception {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_authError_returnFailure() throws Exception {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_generalError_returnFailure() throws Exception {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsername_networkError_returnNetworkError() throws Exception {
        networkError();
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));

    }

    private void success() throws  NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(),anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS,USER_ID,USER_NAME));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mUpdateUsernameHttpEndpointSyncMock).updateUsername(anyString(),anyString());
    }

    private void generalError()throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(),anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"",""));
    }

    private void authError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(),anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR,"",""));
    }

    private void serverError() throws Exception {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(anyString(),anyString()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR,"",""));
    }
}