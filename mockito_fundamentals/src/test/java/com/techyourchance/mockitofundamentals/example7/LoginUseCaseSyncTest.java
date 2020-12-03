package com.techyourchance.mockitofundamentals.example7;

import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache;
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync;
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    public void setup() throws NetworkErrorException {
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncMock,mAuthTokenCacheMock,mEventBusPosterMock);
        success();
    }

    @Test
    public void loginSync_whenSuccess_shouldUsernameAndPasswordPassedToEndpoint() throws NetworkErrorException {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME,PASSWORD);
        verify(mLoginHttpEndpointSyncMock,times(1)).loginSync(ac.capture(),ac.capture());
        List<String> captures = ac.getAllValues();
        Assert.assertThat(captures.get(0),is(USERNAME));
        Assert.assertThat(captures.get(1),is(PASSWORD));
    }

    @Test
    public void loginSync_whenSuccess_shouldAuthTokenCached() {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME,PASSWORD);
        verify(mAuthTokenCacheMock).cacheAuthToken(ac.capture());
        Assert.assertThat(ac.getValue(),is(AUTH_TOKEN));
    }

    @Test
    public void loginSync_whenGeneralError_shouldAuthTokenNotCached() throws NetworkErrorException {
        generalError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_whenServerError_shouldAuthTokenNotCached() throws NetworkErrorException {
        serverError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_whenAuthError_shouldAuthTokenNotCached() throws NetworkErrorException {
        authError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mAuthTokenCacheMock);
    }

    @Test
    public void loginSync_whenSuccess_shouldLoggedInEventPosted() {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME,PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        Assert.assertThat(ac.getValue(),is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_whenGeneralError_shouldNoInteractionWithEventBusPoster() throws NetworkErrorException {
        generalError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_whenAuthError_shouldNoInteractionWithEventBusPoster() throws NetworkErrorException {
        authError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_whenServerError_shouldNoInteractionWithEventBusPoster() throws NetworkErrorException {
        serverError();
        SUT.loginSync(USERNAME,PASSWORD);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void loginSync_whenSuccess_shouldReturnSuccess(){
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertThat(result, is(LoginUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_whenServerError_shouldReturnFailure() throws NetworkErrorException {
        serverError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenAuthError_shouldReturnFailure() throws NetworkErrorException {
        authError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenGeneralError_shouldReturnFailure() throws NetworkErrorException {
        generalError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenNetwork_shouldReturnNetworkError() throws NetworkErrorException {
        networkError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME,PASSWORD);
        Assert.assertThat(result,is(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    private void success() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class),any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,AUTH_TOKEN));
    }

    private void generalError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class),any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,""));
    }

    private void serverError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class),any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,""));
    }

    private void authError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(String.class),any(String.class)))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,""));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(mLoginHttpEndpointSyncMock)
                .loginSync(any(String.class),any(String.class));
    }
}