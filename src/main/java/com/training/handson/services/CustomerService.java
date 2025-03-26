package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.cart.CartResourceIdentifierBuilder;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.AddressBuilder;
import com.commercetools.api.models.customer.*;
import com.commercetools.api.models.customer_group.CustomerGroup;
import com.commercetools.api.models.store.StoreResourceIdentifierBuilder;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.CustomerCreateRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

        if(country != null && !country.isEmpty())
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

        if (anonymousCartId != null && !anonymousCartId.isEmpty()){
            customerSigninBuilder.anonymousCart(CartResourceIdentifierBuilder.of()
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

    public CompletableFuture<ApiHttpResponse<Customer>> addDefaultShippingAddressToCustomer(
            final String customerId,
            final Address address) {

        return getCustomerById(customerId)
                .thenComposeAsync(customerApiHttpResponse ->
                        apiRoot
                                .inStore(storeKey)
                                .customers()
                                .withId(customerId)
                                .post(
                                        customerUpdateBuilder -> customerUpdateBuilder
                                                .version(customerApiHttpResponse.getBody().getVersion())
                                                .plusActions(actionBuilder ->actionBuilder
                                                        .addAddressBuilder()
                                                        .address(address))
                                                .plusActions(actionBuilder -> actionBuilder
                                                        .setDefaultShippingAddressBuilder()
                                                        .addressKey(address.getKey()))
                                )
                                .execute()
                );
    }

    public CompletableFuture<ApiHttpResponse<Customer>> setAddressCustomFields(
            final CustomFieldRequest customFieldRequest) {

        final String customerId = customFieldRequest.getCustomerId();
        final String addressKey = customFieldRequest.getAddressKey();
        final String instructions = customFieldRequest.getInstructions();
        final String time = customFieldRequest.getTime();

        return getCustomerById(customerId)
                .thenApply(ApiHttpResponse::getBody)
                .thenComposeAsync(customer -> {
                    String addressId = getAddressIdByKey(customer, addressKey);
                    return apiRoot
                            .inStore(storeKey)
                            .customers()
                            .withId(customerId)
                            .post(
                                    updateBuilder -> updateBuilder
                                            .version(customer.getVersion())
                                            .plusActions(actionBuilder -> actionBuilder.setAddressCustomTypeBuilder()
                                                    .addressId(addressId)
                                                    .type(typeResourceIdentifierBuilder -> typeResourceIdentifierBuilder.key("address-delivery-instructions"))
                                                    .fields(fieldContainerBuilder -> fieldContainerBuilder
                                                            .addValue("instructions", instructions)
                                                            .addValue("time", time)
                                                    )
                                            )
                            )
                            .execute();
                });
    }

    private String getAddressIdByKey(final Customer customer, final String addressKey){
       return customer.getAddresses().stream().filter(address -> address.getKey().equals(addressKey)).findFirst().map(Address::getId).orElse(null);
    }

    public CompletableFuture<ApiHttpResponse<CustomerToken>> createEmailVerificationToken(
            final ApiHttpResponse<CustomerSignInResult> customerSignInResultApiHttpResponse,
            final long timeToLiveInMinutes
    ) {

        final Customer customer = customerSignInResultApiHttpResponse.getBody().getCustomer();

        return
            apiRoot
                .inStore(storeKey)
                .customers()
                .emailToken()
                .post(
                    customerCreateEmailTokenBuilder -> customerCreateEmailTokenBuilder
                        .id(customer.getId())
                        .ttlMinutes(timeToLiveInMinutes)
                )
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomerToken>> createEmailVerificationToken(final Customer customer, final long timeToLiveInMinutes) {

        return
            apiRoot
                .inStore(storeKey)
                .customers()
                .emailToken()
                .post(
                    customerCreateEmailTokenBuilder -> customerCreateEmailTokenBuilder
                        .id(customer.getId())
                        .ttlMinutes(timeToLiveInMinutes)
                )
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> verifyEmail(final ApiHttpResponse<CustomerToken> customerTokenApiHttpResponse) {

        final CustomerToken customerToken = customerTokenApiHttpResponse.getBody();

        return
            apiRoot
                .inStore(storeKey)
                .customers()
                .emailConfirm()
                .post(
                    customerEmailVerifyBuilder ->customerEmailVerifyBuilder
                        .tokenValue(customerToken.getValue())
                )
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> verifyEmail(final CustomerToken customerToken) {

        return
            apiRoot
                .inStore(storeKey)
                .customers()
                .emailConfirm()
                .post(
                    customerEmailVerifyBuilder ->customerEmailVerifyBuilder
                        .tokenValue(customerToken.getValue())
                    )
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomerGroup>> getCustomerGroupByKey(String customerGroupKey) {
        return
            apiRoot
                .customerGroups()
                .withKey(customerGroupKey)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<Customer>> assignCustomerToCustomerGroup(
            final String customerKey,
            final String customerGroupKey) {

        return getCustomerByKey(customerKey)
                .thenComposeAsync(customerApiHttpResponse ->
                        apiRoot
                                .inStore(storeKey)
                                .customers()
                                .withKey(customerKey)
                                .post(
                                        customerUpdateBuilder -> customerUpdateBuilder
                                                .version(customerApiHttpResponse.getBody().getVersion())
                                                .plusActions(
                                                        customerUpdateActionBuilder -> customerUpdateActionBuilder
                                                                .setCustomerGroupBuilder()
                                                                .customerGroup(customerGroupResourceIdentifierBuilder -> customerGroupResourceIdentifierBuilder.key(customerGroupKey))
                                                )
                                )
                                .execute()
                );
    }

}
