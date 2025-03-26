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
    public String getPassword() {
        return password;
    }
    public String getKey() {
        return key;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getCountry() {
        return country;
    }
    public String getAnonymousCartId() {
        return anonymousCartId;
    }
    public Boolean isDefaultShippingAddress() {return defaultShippingAddress;}
    public String getRegion() {return region;}
    public String getCity() {return city;}
    public Boolean isDefaultBillingAddress() {return defaultBillingAddress;}
    public String getStreetName() {return streetName;}
    public String getStreetNumber() {return streetNumber;}

}
