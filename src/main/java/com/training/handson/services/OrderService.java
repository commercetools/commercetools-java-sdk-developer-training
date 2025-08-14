package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.Order;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.OrderRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private ProjectApiRoot apiRoot;

    @Autowired
    private CustomerService customerService;

    public CompletableFuture<ApiHttpResponse<Order>> getOrderById(
            final String storeKey,
            final String orderId) {

            return apiRoot
                    .inStore(storeKey)
                    .orders()
                    .withId(orderId)
                    .get()
                    .execute();
    }

    public CompletableFuture<ApiHttpResponse<Order>> getOrderByOrderNumber(
            final String storeKey,
            final String orderNumber) {

        return apiRoot
                .inStore(storeKey)
                .orders()
                .withOrderNumber(orderNumber)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Order>> createOrder(
            final String storeKey,
            final OrderRequest orderRequest) {

        // TODO: Create an order using the cardId and version in the request
            return CompletableFuture.completedFuture(
                    new ApiHttpResponse<>(501, null, Order.of())
            );
    }

    public CompletableFuture<ApiHttpResponse<Order>> setCustomFields(
            final String storeKey,
            final String orderNumber,
            final CustomFieldRequest customFieldRequest) {

        // TODO: Update the order with custom delivery instructions
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, Order.of())
        );
    }

    public CompletableFuture<ApiHttpResponse<Cart>> replicateOrderByOrderNumber(
            final String storeKey,
            final String orderNumber) {

        return getOrderByOrderNumber(storeKey, orderNumber)
            .thenComposeAsync(orderApiHttpResponse -> apiRoot
                .inStore(storeKey)
                .carts()
                .replicate()
                .post(
                        replicaCartDraftBuilder -> replicaCartDraftBuilder
                                .reference(referenceBuilder -> referenceBuilder.cartBuilder().id(orderApiHttpResponse.getBody().getCart().getId()))
                )
                .execute());
    }

}
