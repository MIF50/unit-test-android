package com.techyourchance.testdrivendevelopment.example9;

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSync.UseCaseResult;
import com.techyourchance.testdrivendevelopment.example9.networking.AddToCartHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;
import junit.framework.TestCase;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncTest extends TestCase {

    private final static String OFFER_ID = "offer_id";
    private final static int AMOUNT = 4;

    @Mock
    private AddToCartUseCaseSync SUT;

    @Mock
    private AddToCartHttpEndpointSync mAddToCartHttpEndpointSyncMock;

    @Before
    public void setup() throws NetworkErrorException {
        SUT = new AddToCartUseCaseSync(mAddToCartHttpEndpointSyncMock);
        success();
    }

    @Test
    public void addToCart_whenSuccess_shouldPassParametersToEndpoint() throws NetworkErrorException {
        // Arrange
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        // Act
        SUT.addToCart(OFFER_ID,AMOUNT);
        verify(mAddToCartHttpEndpointSyncMock).addToCartSync(ac.capture());
        // Assert
        assertThat(ac.getValue(), instanceOf(CartItemScheme.class));
        assertThat(ac.getValue().getOfferId(),is(OFFER_ID));
        assertThat(ac.getValue().getAmount(),is(AMOUNT));
    }

    @Test
    public void addToCart_whenSuccess_shouldReturnSuccess() {
        // Arrange
        // Act
        UseCaseResult result = SUT.addToCart(OFFER_ID,AMOUNT);
        // Assert
        assertThat(result,is(UseCaseResult.SUCCESS));
    }

    @Test
    public void addToCart_whenAuthError_shouldReturnFailure() throws NetworkErrorException {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.addToCart(OFFER_ID,AMOUNT);
        // Assert
        assertThat(result,is(UseCaseResult.FAILURE));
    }

    @Test
    public void addToCart_whenGeneralError_shouldReturnFailure() throws NetworkErrorException {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.addToCart(OFFER_ID,AMOUNT);
        // Assert
        assertThat(result,is(UseCaseResult.FAILURE));
    }

    @Test
    public void addToCart_whenNetworkError_shouldReturnNetworkError() throws NetworkErrorException {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.addToCart(OFFER_ID,AMOUNT);
        // Assert
        assertThat(result,is(UseCaseResult.NETWORK_ERROR));
    }


    private void success() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.SUCCESS);
    }

    private void authError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.AUTH_ERROR);
    }

    private void generalError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(AddToCartHttpEndpointSync.EndpointResult.GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenThrow(new NetworkErrorException());
    }
}