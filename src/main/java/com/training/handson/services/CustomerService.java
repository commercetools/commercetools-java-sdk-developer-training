package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.CartResourceIdentifierBuilder;
import com.commercetools.api.models.common.AddressBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.store.StoreResourceIdentifierBuilder;
import com.training.handson.dto.CustomerCreateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {

    @Autowired
    private ProjectApiRoot apiRoot;

    @Autowired
    private String storeKey;


    public CompletableFuture<ApiHttpResponse<Customer>> getCustomerByKey(String customerKey) {
        return apiRoot
                .inStore(storeKey)
                .customers()
                .withKey(customerKey)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> getCustomerById(String customerId) {
        return apiRoot
                .inStore(storeKey)
                .customers()
                .withId(customerId)
                .get()
                .execute();
    }


    public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> createCustomer(
            final CustomerCreateRequest customerCreateRequest) {

        final String email = customerCreateRequest.getEmail();
        final String password = customerCreateRequest.getPassword();
        final String key = customerCreateRequest.getKey();
        final String firstName = customerCreateRequest.getFirstName();
        final String lastName = customerCreateRequest.getLastName();
        final String streetNumber = customerCreateRequest.getStreetNumber();
        final String streetName = customerCreateRequest.getStreetName();
        final String city = customerCreateRequest.getCity();
        final String region = customerCreateRequest.getRegion();
        final String country = customerCreateRequest.getCountry();
        final String anonymousCartId = customerCreateRequest.getAnonymousCartId();


        CustomerDraftBuilder builder = CustomerDraftBuilder.of()
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .key(key)
                .stores(StoreResourceIdentifierBuilder.of().key(storeKey).build());

        if(StringUtils.isNotEmpty(country))
            builder.addresses(
                    AddressBuilder.of()
                            .key("ct" + System.nanoTime())
                            .firstName(firstName)
                            .lastName(lastName)
                            .streetNumber(streetNumber)
                            .streetName(streetName)
                            .city(city)
                            .region(region)
                            .country(country)
                            .email(email)
                            .build());

        if(customerCreateRequest.isDefaultBillingAddress())
            builder.defaultBillingAddress(0);

        if(customerCreateRequest.isDefaultShippingAddress())
            builder.defaultShippingAddress(0);


        if(StringUtils.isNotEmpty(anonymousCartId))
            builder.anonymousCart(cartResourceIdentifierBuilder ->
                    cartResourceIdentifierBuilder.id(anonymousCartId));

        return apiRoot
            .inStore(storeKey)
            .customers()
            .post(builder.build())
            .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomerSignInResult>> loginCustomer(
            final CustomerCreateRequest customerCreateRequest) {

        final String email = customerCreateRequest.getEmail();
        final String password = customerCreateRequest.getPassword();
        final String anonymousCartId = customerCreateRequest.getAnonymousCartId();

        CustomerSigninBuilder customerSigninBuilder = CustomerSigninBuilder.of()
                .email(email)
                .password(password);

        if (StringUtils.isNotEmpty(anonymousCartId)){
                customerSigninBuilder
                        .anonymousCart(CartResourceIdentifierBuilder.of()
                                    .id(anonymousCartId)
                                    .build()
                            )
                        .anonymousCartSignInMode(AnonymousCartSignInMode.USE_AS_NEW_ACTIVE_CUSTOMER_CART);
        }

        return apiRoot
                .inStore(storeKey)
                .login()
                .post(customerSigninBuilder.build())
                .execute();
    }

}
