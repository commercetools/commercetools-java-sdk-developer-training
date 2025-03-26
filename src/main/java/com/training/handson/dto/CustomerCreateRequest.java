package com.training.handson.dto;

public class CustomerCreateRequest {

    private String email;
    private String password;
    private String key;
    private String firstName;
    private String lastName;
    private String anonymousCartId;

    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    private String country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAnonymousCartId() {
        return anonymousCartId;
    }

    public Boolean isDefaultShippingAddress() {return defaultShippingAddress;}

    public void setDefaultShippingAddress(Boolean defaultShippingAddress) {this.defaultShippingAddress = defaultShippingAddress;}

    public void setAnonymousCartId(String anonymousCartId) {
        this.anonymousCartId = anonymousCartId;
    }

    public String getRegion() {return region;}

    public void setRegion(String region) {this.region = region;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public Boolean isDefaultBillingAddress() {return defaultBillingAddress;}

    public void setDefaultBillingAddress(Boolean defaultBillingAddress) {this.defaultBillingAddress = defaultBillingAddress;}

    public String getStreetName() {return streetName;}

    public void setStreetName(String streetName) {this.streetName = streetName;}

    public String getStreetNumber() {return streetNumber;}

    public void setStreetNumber(String streetNumber) {this.streetNumber = streetNumber;}

}
