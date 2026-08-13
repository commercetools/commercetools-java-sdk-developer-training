package com.training.handson.dto;

public class CustomerCreateRequest {

    private String email;
    private String password;
    private String key;
    private String firstName;
    private String lastName;
    private String anonymousCartId;

    private AddressRequest address;
    private Boolean defaultShippingAddress;
    private Boolean defaultBillingAddress;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAnonymousCartId() {
        return anonymousCartId;
    }
    public void setAnonymousCartId(String anonymousCartId) {
        this.anonymousCartId = anonymousCartId;
    }
    public Boolean isDefaultShippingAddress() {return defaultShippingAddress;}
    public void setDefaultShippingAddress(Boolean defaultShippingAddress) {this.defaultShippingAddress = defaultShippingAddress;}
    public Boolean isDefaultBillingAddress() {return defaultBillingAddress;}
    public void setDefaultBillingAddress(Boolean defaultBillingAddress) {this.defaultBillingAddress = defaultBillingAddress;}
    public AddressRequest getAddress() {return address;}
    public void setAddress(AddressRequest address) {this.address = address;}

}
