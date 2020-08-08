package com.techyourchance.mockitofundamentals.example7;

import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUseCaseSyncTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String AUTH_TOKEN = "authToken";

    @Mock
    private LoginHttpEndpointSync mLoginHttpEndpointSyncMock;

    @Mock
    private AuthTokenCache mAuthTokenCacheMock;

    @Mock
    private EventBusPoster mEventBusPosterMock;

    private LoginUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock);
        success();
    }

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mLoginHttpEndpointSyncMock, times(1)).loginSync(ac.capture(), ac.capture());

        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USERNAME));
        assertThat(captures.get(1), is(PASSWORD));
    }

    @Test
    public void loginSync_success_authTokenCached() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mAuthTokenCacheMock).cacheAuthToken(ac.capture());
        assertThat(ac.getValue(), is(AUTH_TOKEN));
    }

    @Test
    public void loginSync_generalError_authTokenNotCached() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_success_loggedInEventPosted() throws Exception {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_success_returnSuccess() throws Exception {
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_serverError_returnFailure() throws Exception {
        serverError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_authError_returnFailure() throws Exception {
        authError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_generalError_returnFailure() throws Exception {
        generalError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_networkError_returnNetworkError() throws Exception {
        networkError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result, is(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
                .when(mLoginHttpEndpointSyncMock)
                .loginSync(any(String.class), any(String.class));
    }

    private void success() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock
                .loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }

    private void generalError() throws Exception {
        when(mLoginHttpEndpointSyncMock
                .loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, ""));
    }

    private void authError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, ""));
    }

    private void serverError() throws Exception {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class), any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, ""));
    }

}