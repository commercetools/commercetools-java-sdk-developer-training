package com.training.handson.dto;

import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.BaseAddress;

public class AddressRequest {

    private String cartId;
    private Address address;

    public Address getAddress() {return address;}
    public String getCartId() { return cartId; }

}
