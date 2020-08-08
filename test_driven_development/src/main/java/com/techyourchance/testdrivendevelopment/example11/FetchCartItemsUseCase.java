package com.techyourchance.testdrivendevelopment.example11;

import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.CartItemSchema;
import com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchCartItemsUseCase {

    public interface Listener {
        void onCartItemsFetched(List<CartItem> capture);
        void onFetchCartItemsFailed();
    }

    private final List<Listener> mListeners = new ArrayList<>();
    private final GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint;

     FetchCartItemsUseCase(GetCartItemsHttpEndpoint getCartItemsHttpEndpoint) {
        mGetCartItemsHttpEndpoint = getCartItemsHttpEndpoint;
    }

    void fetchCartItemsAndNotify(int limit) {
        mGetCartItemsHttpEndpoint.getCartItems(limit, new GetCartItemsHttpEndpoint.Callback() {

            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItemsSchema) {
                notifySucceeded(cartItemsSchema);
            }

            @Override
            public void onGetCartItemsFailed(GetCartItemsHttpEndpoint.FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                    case NETWORK_ERROR:
                        notifyFailed();
                        break;
                }
            }
        });
    }

    private void notifySucceeded(List<CartItemSchema> cartItemsSchema) {
        for (Listener listener : mListeners) {
            listener.onCartItemsFetched(cartItemsFromSchemas(cartItemsSchema));
        }
    }

    private void notifyFailed() {
        for (Listener listener : mListeners) {
            listener.onFetchCartItemsFailed();
        }
    }

    private List<CartItem> cartItemsFromSchemas(List<CartItemSchema> cartItemSchemas) {
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemSchema schema : cartItemSchemas) {
            cartItems.add(new CartItem(
                    schema.getId(),
                    schema.getTitle(),
                    schema.getDescription(),
                    schema.getPrice()
            ));
        }
        return cartItems;
    }

     void registerListener(Listener listener) {
        mListeners.add(listener);
    }

     void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }

}
