package com.techyourchance.testdrivendevelopment.example9;

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync.EndpointResult.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncTest {

    // region constants ----------------------------------------------------------------------------

    private static final String OFFER_ID = "offerId";
    private static final int AMOUNT = 4;

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock AddToCartHttpEndpointSync mAddToCartHttpEndpointSyncMock;
    // endregion helper fields ---------------------------------------------------------------------

    private AddToCartUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new AddToCartUseCaseSync(mAddToCartHttpEndpointSyncMock);
        success();
    }

    @Test
    public void addToCartSync_correctParametersPassedToEndpoint() throws Exception {
        // Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        // Act
        SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        verify(mAddToCartHttpEndpointSyncMock).addToCartSync(ac.capture());
        CartItemScheme cartItemScheme = ac.getValue();
        assertThat(cartItemScheme.getOfferId(), is(OFFER_ID));
        assertThat(cartItemScheme.getAmount(), is(AMOUNT));
    }

    @Test
    public void addToCartSync_success_returnSuccess() throws Exception {
        // Arrange
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void addToCartSync_authError_returnFailure() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void addToCartSync_generalError_returnFailure() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void addToCartSync_networkError_returnNetworkError() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    // region helper methods -----------------------------------------------------------------------

    private void success() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(SUCCESS);
    }

    private void authError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AUTH_ERROR);
    }

    private void generalError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenThrow(new NetworkErrorException());
    }

    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------

}