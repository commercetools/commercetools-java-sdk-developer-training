package com.training.handson.dto;

public class CartCreateRequest {

    private String sku;
    private Long quantity;
    private String country;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    private String currency;

    public String getSku() {
        return sku;
    }
    public Long getQuantity() {
        return quantity;
    }

}