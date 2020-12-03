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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    private final static String USER_ID = "user_id";
    private final static String USER_NAME = "user_name";

    private UpdateUsernameUseCaseSync SUT;
    @Mock
    private UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    @Mock
    private UsersCache mUserCacheMock;
    @Mock
    private EventBusPoster mEventBusPosterMock;

    @Before
    public void setup() throws NetworkErrorException {
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUserCacheMock, mEventBusPosterMock);
        success();
    }

    @Test
    public void updateUsernameSync_whenSuccess_shouldPassUserIdAndUsernameToEndpoint() throws NetworkErrorException {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.updateUsernameSync(USER_ID, USER_NAME);
        // Assert
        verify(mUpdateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USER_NAME));
    }

    @Test
    public void updateUsernameSync_whenSuccess_shouldUserCached(){
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verify(mUserCacheMock).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertThat(cachedUser.getUserId(),is(USER_ID));
        assertThat(cachedUser.getUsername(),is(USER_NAME));
        assertThat(ac.getValue(),is(instanceOf(User.class)));
    }

    @Test
    public void updateUsernameSync_whenAuthError_shouldUserNotCached() throws NetworkErrorException {
        // Arrange
        authError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsernameSync_whenServerError_shouldUserNotCached() throws NetworkErrorException {
        // Arrange
        serverError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsernameSync_whenGeneralError_shouldUserNotCached() throws NetworkErrorException {
        // Arrange
        generalError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mUserCacheMock);
    }

    @Test
    public void updateUsernameSync_whenSuccess_shouldTriggerEventBusPoster() {
        // Arrange
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertThat(ac.getValue(),is(instanceOf(UserDetailsChangedEvent.class)));
    }

    @Test
    public void updateUsernameSync_whenAuthError_shouldNotTriggerEventBustPoster() throws NetworkErrorException {
        // Arrange
        authError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_whenServerError_shouldNotTriggerEventBusPoster() throws NetworkErrorException {
        // Arrange
        serverError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_whenGeneralError_shouldNotTriggerEventBusPoster() throws NetworkErrorException {
        // Arrange
        generalError();
        // Act
        SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_whenSuccess_shouldReturnSuccess() {
        // Act
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUsernameSync_whenAuthError_shouldReturnFailure() throws NetworkErrorException {
        // Arrange
        authError();
        // Act
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_whenServerError_shouldReturnFailure() throws NetworkErrorException {
        // Arrange
        serverError();
        // Act
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_whenGeneralError_shouldReturnFailure() throws NetworkErrorException {
        // Arrange
        generalError();
        // Act
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_whenNetworkError_shouldReturnNetworkError() throws NetworkErrorException {
        // Arrange
        networkError();
        // Act
        UpdateUsernameUseCaseSync.UseCaseResult result = SUT.updateUsernameSync(USER_ID,USER_NAME);
        // Assert
        assertThat(result,is(UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR));
    }

    private void success() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS,USER_ID,USER_NAME));
    }

    private void authError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR,"",""));

    }

    private void serverError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR,"",""));
    }

    private void generalError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(String.class),any(String.class)))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"",""));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(mUpdateUsernameHttpEndpointSyncMock).updateUsername(any(String.class),any(String.class));
    }
}