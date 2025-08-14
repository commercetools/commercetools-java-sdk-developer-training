package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.training.handson.dto.CustomerCreateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Customer>> getCustomerByKey(
            final String storeKey,
            final String customerKey) {
        return apiRoot
                .inStore(storeKey)
                .customers()
                .withKey(customerKey)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> getCustomerById(
            final String storeKey,
            String customerId) {
        return apiRoot
                .inStore(storeKey)
                .customers()
                .withId(customerId)
                .get()
                .execute();
    }


    public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> createCustomer(
            final String storeKey,
            final CustomerCreateRequest customerCreateRequest) {

        // TODO: Create (signup) a customer and assign anonymous cart in the request to them
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, CustomerSignInResult.of())
        );
    }

    public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> loginCustomer(
            final String storeKey,
            final CustomerCreateRequest customerCreateRequest) {

        // TODO: Login (signin) a customer and assign anonymous cart in the request to them
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, CustomerSignInResult.of())
        );
    }

}
