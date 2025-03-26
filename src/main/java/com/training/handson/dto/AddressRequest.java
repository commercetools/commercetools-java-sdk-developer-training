package com.training.handson.dto;

import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.BaseAddress;

public class AddressRequest {

    private String cartId;
    private String customerId;
    private Address address;
    private Boolean defaultShippingAddress;

    public Boolean isDefaultShippingAddress() {return defaultShippingAddress;}

    public void setDefaultShippingAddress(Boolean defaultShippingAddress) {this.defaultShippingAddress = defaultShippingAddress;}

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

    public String getCartId() { return cartId; }

    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getCustomerId() { return customerId; }

    public void setCustomerId(String customerId) { this.customerId = customerId; }

}
