package com.techyourchance.testdoublesfundamentals.example4;

import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster;
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginUseCaseSyncTest {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String AUTH_TOKEN = "auth_token";

    private LoginUseCaseSync SUT;
    private LoginHttpEndpointSyncTD mLoginHttpEndpointSyncTD;
    private AuthTokenCacheTD mAuthTokenCacheTD;
    private EventBusPosterTD mEventBusPosterTD;

    @Before
    public void setup(){
        mLoginHttpEndpointSyncTD = new LoginHttpEndpointSyncTD();
        mAuthTokenCacheTD = new AuthTokenCacheTD();
        mEventBusPosterTD = new EventBusPosterTD();
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncTD,mAuthTokenCacheTD, mEventBusPosterTD);
    }

    @Test
    public void loginSync_whenLoginSuccess_shouldPassedUsernameAndPasswordToEndPoint(){
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mLoginHttpEndpointSyncTD.username, is(USERNAME));
        assertThat(mLoginHttpEndpointSyncTD.password,is(PASSWORD));
    }

    @Test
    public void loginSync_whenLoginSuccess_shouldCachedAuthToken(){
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTD.getAuthToken(),is(AUTH_TOKEN));
    }

    @Test
    public void loginSync_whenLoginSuccess_shouldTriggerLoginEventBus(){
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTD.event, is(instanceOf(LoggedInEvent.class)));
    }

    @Test
    public void loginSync_whenLoginSuccess_shouldReturnSuccess(){
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result,is(LoginUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void loginSync_whenLoginAuthError_shouldAuthTokenNotChange(){
        mLoginHttpEndpointSyncTD.isAuthError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTD.getAuthToken(),is(""));
    }

    @Test
    public void loginSync_whenLoginGeneralError_shouldAuthTokenNotChange(){
        mLoginHttpEndpointSyncTD.isGeneralError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTD.getAuthToken(),is(""));
    }

    @Test
    public void loginSync_whenLoginServerError_shouldAuthTokenNotChange(){
        mLoginHttpEndpointSyncTD.isServerError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mAuthTokenCacheTD.getAuthToken(),is(""));
    }

    @Test
    public void loginSync_whenLoginAuthError_shouldNotTriggerLoginEventBus() {
        mLoginHttpEndpointSyncTD.isAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertThat(mEventBusPosterTD.triggerCount, is(0));
    }

    @Test
    public void loginSync_whenLoginGeneralError_shouldNotTriggerLoginEventBus(){
        mLoginHttpEndpointSyncTD.isGeneralError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTD.triggerCount,is(0));
    }

    @Test
    public void loginSync_whenLoginServerError_shouldNotTriggerLoginEventBus(){
        mLoginHttpEndpointSyncTD.isServerError = true;
        SUT.loginSync(USERNAME,PASSWORD);
        assertThat(mEventBusPosterTD.triggerCount,is(0));
    }

    @Test
    public void loginSync_whenLoginAuthError_shouldReturnFailure(){
        mLoginHttpEndpointSyncTD.isAuthError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenLoginGeneralError_shouldReturnFailure(){
        mLoginHttpEndpointSyncTD.isGeneralError= true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenLoginServerError_shouldReturnFailure(){
        mLoginHttpEndpointSyncTD.isServerError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result,is(LoginUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void loginSync_whenLoginNetworkError_shouldReturnNetworkError(){
        mLoginHttpEndpointSyncTD.isNetworkError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertThat(result,is(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    // helper classes -----------------------------------------
    private static class LoginHttpEndpointSyncTD implements LoginHttpEndpointSync {

        private String username = "";
        private String password = "";

        private boolean isAuthError;
        private boolean isGeneralError;
        private boolean isServerError;
        private boolean isNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            this.username = username;
            this.password = password;

            if (isAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR,"");
            } else if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR,"");
            } else if (isNetworkError){
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS,AUTH_TOKEN);
            }
        }
    }

    private static class  AuthTokenCacheTD implements AuthTokenCache {

        private String authToken = "";

        @Override
        public void cacheAuthToken(String authToken) {
            this.authToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return authToken;
        }
    }

    private static class EventBusPosterTD implements EventBusPoster {

        private Object event;
        private int triggerCount = 0;

        @Override
        public void postEvent(Object event) {
            this.event = event;
            triggerCount++;
        }
    }
}