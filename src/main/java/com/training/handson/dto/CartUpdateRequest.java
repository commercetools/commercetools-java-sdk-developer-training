package com.training.handson.dto;

public class CartUpdateRequest {
    private String sku;

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    private Long quantity;

    public String getSku() {
        return sku;
    }
    public Long getQuantity() {
        return quantity;
    }

}
