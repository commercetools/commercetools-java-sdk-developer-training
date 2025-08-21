package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.shipping_method.ShippingMethod;
import com.commercetools.api.models.shipping_method.ShippingMethodPagedQueryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import io.vrap.rmf.base.client.ApiHttpResponse;
import io.vrap.rmf.base.client.utils.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShippingService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethods() {

        return apiRoot
                .shippingMethods()
                .get()
                .withExpand("zoneRates[*].zone")
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethod>> getShippingMethodByKey(
            final String key) {

        // TODO: Return a list of shipping methods valid for a country
            return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, ShippingMethod.of())
            );
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethodsByCountry(
            final String countryCode) {

        // TODO: Return a list of shipping methods valid for a country
        return CompletableFuture.completedFuture(
            new ApiHttpResponse<>(501, null, ShippingMethodPagedQueryResponse.of())
        );
    }

    public CompletableFuture<ApiHttpResponse<ShippingMethodPagedQueryResponse>> getShippingMethodsByInStoreCart(
            final String storeKey,
            final String cartId) {

        return apiRoot
            .inStore(storeKey)
            .shippingMethods()
            .matchingCart()
            .get()
            .withCartId(cartId)
            .withExpand("zoneRates[*].zone")
            .execute();
    }

    public CompletableFuture<ApiHttpResponse<JsonNode>> checkShippingMethodExistence(
            final String key) {

        // TODO: Return true if a shipping method by key exists
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, JsonUtils.toJsonNode("{}"))
        );
    }
}
