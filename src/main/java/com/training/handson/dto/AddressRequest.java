package com.training.handson.dto;

import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.BaseAddress;

public class AddressRequest {

    private String cartId;
    private String customerId;
    private Address address;
    private Boolean defaultShippingAddress;

    public Boolean isDefaultShippingAddress() {return defaultShippingAddress;}
    public Address getAddress() {return address;}
    public String getCartId() { return cartId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

}
