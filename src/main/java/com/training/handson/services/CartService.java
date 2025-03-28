package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartAddDiscountCodeActionBuilder;
import com.commercetools.api.models.cart.CartUpdateAction;
import com.commercetools.api.models.cart.InventoryMode;
import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.training.handson.dto.AddressRequest;
import com.training.handson.dto.CartUpdateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CartService {

    @Autowired
    private ProjectApiRoot apiRoot;

    @Autowired
    private String storeKey;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CustomerService customerService;

    public CompletableFuture<ApiHttpResponse<Cart>> getCartById(final String cartId) {

            return apiRoot
                    .inStore(storeKey)
                    .carts()
                    .withId(cartId)
                    .get()
                    .execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> updateCart(CartUpdateRequest cartUpdateRequest){

        final String cartId = cartUpdateRequest.getCartId();
        final String customerId = cartUpdateRequest.getCustomerId();
        final String sku = cartUpdateRequest.getSku();
        final Long quantity = cartUpdateRequest.getQuantity();

        if (StringUtils.isNotEmpty(cartId)) {
            return addProductToCartBySkusAndChannel(cartId, sku, quantity);
        } else if (StringUtils.isNotEmpty(customerId)) {
            return createCart(sku, quantity, customerId);
        } else {
            return createCart(sku, quantity);
        }
    }

    public CompletableFuture<ApiHttpResponse<Cart>> createCart(
            final String sku,
            final Long quantity
//            final String supplyChannelKey,
//            final String distChannelKey
    ) {

        // TODO: Create a cart with anonymousId and add SKU as a line item
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, Cart.of())
        );
    }

    private CompletableFuture<ApiHttpResponse<Cart>> createCart(
            final String sku,
            final Long quantity,
            final String customerId
//            final String supplyChannelKey,
//            final String distChannelKey
    ) {

        return
                customerService.getCustomerById(customerId).thenApply(ApiHttpResponse::getBody)
                        .thenCombineAsync(storeService.getCurrentStore().thenApply(ApiHttpResponse::getBody),
                                ((customer, store) -> {
                                    String countryCode = store.getCountries().get(0).getCode();
                                    String currencyCode = getCurrencyCodeByCountry(countryCode);
                                    return apiRoot
                                            .inStore(storeKey)
                                            .carts()
                                            .post(
                                                    cartDraftBuilder -> cartDraftBuilder
                                                            .currency(currencyCode)
                                                            .deleteDaysAfterLastModification(90L)
                                                            .customerEmail(customer.getEmail())
                                                            .customerId(customer.getId())
                                                            .country(countryCode)
                                                            .shippingAddress(customer.getDefaultShippingAddress())
                                                            .addLineItems(lineItemDraftBuilder -> lineItemDraftBuilder
                                                                    .sku(sku)
//                                                            .supplyChannel(channelResourceIdentifierBuilder ->
//                                                                    channelResourceIdentifierBuilder.key(supplyChannelKey))
//                                                            .distributionChannel(channelResourceIdentifierBuilder ->
//                                                                    channelResourceIdentifierBuilder.key(distChannelKey))
                                                                    .quantity(quantity)
                                                                    .build())
                                                            .inventoryMode(InventoryMode.NONE)
                                            )
                                            .execute();
                                }
                                )).join();
    }

    private String getCurrencyCodeByCountry(final String countryCode){
        return switch (countryCode) {
            case "US" -> "USD";
            case "UK" -> "GBP";
            default -> "EUR";
        };
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addProductToCartBySkusAndChannel(
            final String cartId,
            final String sku,
            final Long quantity
//            final String supplyChannelKey,
//            final String distChannelKey
    ) {

        // TODO: Add SKU to the cart
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, Cart.of())
        );
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addDiscountToCart(
            final String cartId,
            final String code) {

        return this.getCartById(cartId)
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
            final AddressRequest addressRequest) {

        // TODO: Set Shipping address on the cart
        // TODO: Set email on the cart
        // TODO: Set default Shipping Method (update setShippingMethod, if needed)
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, Cart.of())
        );

    }


    public CompletableFuture<ApiHttpResponse<Cart>> recalculate(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        final Cart cart = cartApiHttpResponse.getBody();
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
    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShippingMethod(final Cart cart) {

        final ShippingMethod shippingMethod =
            apiRoot
                .shippingMethods()
                .matchingCart()
                .get()
                .withCartId(cart.getId())
                .executeBlocking()
                .getBody().getResults().get(0);
        return apiRoot
            .inStore(storeKey)
            .carts()
            .withId(cart.getId())
            .post(
                cartUpdateBuilder -> cartUpdateBuilder
                    .version(cart.getVersion())
                    .plusActions(
                        cartUpdateActionBuilder -> cartUpdateActionBuilder
                            .setShippingMethodBuilder()
                            .shippingMethod(
                                shippingMethodResourceIdentifierBuilder -> shippingMethodResourceIdentifierBuilder
                                    .id(shippingMethod.getId())
                            )
                    )
            )
            .execute();
    }

}
