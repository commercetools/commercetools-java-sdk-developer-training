package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartAddDiscountCodeActionBuilder;
import com.commercetools.api.models.cart.CartUpdateAction;
import com.commercetools.api.models.common.Address;
import com.training.handson.dto.CartCreateRequest;
import com.training.handson.dto.CartUpdateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CartService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Cart>> getCartById(final String storeKey, final String cartId) {

            return apiRoot
                    .inStore(storeKey)
                    .carts()
                    .withId(cartId)
                    .get()
                    .execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> createCart(
            final String storeKey,
            final CartCreateRequest cartCreateRequest
            ) {

        // TODO: Create a cart with anonymousId and add SKU as a line item
            return CompletableFuture.completedFuture(
                    new ApiHttpResponse<>(501, null, Cart.of())
            );
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addLineItem(
            final String storeKey,
            final String cartId,
            CartUpdateRequest cartUpdateRequest){

        // TODO: Add SKU to the cart
            return CompletableFuture.completedFuture(
                    new ApiHttpResponse<>(501, null, Cart.of())
            );
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addDiscountCode(
            final String storeKey,
            final String cartId,
            final String code) {

        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    CartUpdateAction cartUpdateAction =
                        CartAddDiscountCodeActionBuilder.of()
                            .code(code)
                            .build();

                    return
                            apiRoot
                                    .inStore(storeKey)
                                    .carts()
                                    .withId(cart.getId())
                                    .post(
                                        cartUpdateBuilder -> cartUpdateBuilder
                                            .version(cart.getVersion())
                                            .actions(cartUpdateAction)
                                    )
                                    .execute();
                });
    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShippingAddress(
            final String storeKey,
            final String cartId,
            final Address address) {

        // TODO: Set Shipping address on the cart
        // TODO: Set email on the cart
        // TODO: Set default Shipping Method (update setShippingMethod, if needed)
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, Cart.of())
        );

    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShippingMethod(
            final String storeKey,
            final String cartId,
            final String shippingMethodId) {

        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    return apiRoot
                            .inStore(storeKey)
                            .carts()
                            .withId(cartId)
                            .post(
                                    cartUpdateBuilder -> cartUpdateBuilder
                                            .version(cart.getVersion())
                                            .plusActions(
                                                    cartUpdateActionBuilder -> cartUpdateActionBuilder
                                                            .setShippingMethodBuilder()
                                                            .shippingMethod(
                                                                    shippingMethodResourceIdentifierBuilder -> shippingMethodResourceIdentifierBuilder
                                                                            .id(shippingMethodId)
                                                            )
                                            )
                            )
                            .execute();
                });
    }

    public CompletableFuture<ApiHttpResponse<Cart>> recalculate(final String storeKey, final String cartId) {

        return this.getCartById(storeKey, cartId)
                .thenApply(ApiHttpResponse::getBody)
                .thenCompose(cart -> {
                    return
                            apiRoot
                                    .inStore(storeKey)
                                    .carts()
                                    .withId(cart.getId())
                                    .post(
                                            cartUpdateBuilder -> cartUpdateBuilder
                                                    .version(cart.getVersion())
                                                    .plusActions(
                                                            cartUpdateActionBuilder -> cartUpdateActionBuilder
                                                                    .recalculateBuilder()
                                                                    .updateProductData(true)
                                                    )
                                    )
                                    .execute();
                });

    }

}
