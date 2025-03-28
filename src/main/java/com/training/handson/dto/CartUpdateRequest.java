package com.training.handson.dto;

public class CartUpdateRequest {

    private String cartId;
    private String customerId;
    private String sku;
    private Long quantity;
    private String supplyChannel;
    private String distributionChannel;
    private String code;

    public String getCartId() { return cartId; }
    public String getCustomerId() {return customerId;}
    public String getSku() {
        return sku;
    }
    public Long getQuantity() {
        return quantity;
    }
    public String getSupplyChannel() {
        return supplyChannel;
    }
    public void setSupplyChannel(String supplyChannel) {
        this.supplyChannel = supplyChannel;
    }
    public String getDistributionChannel() {
        return distributionChannel;
    }
    public void setDistributionChannel(String distributionChannel) {
        this.distributionChannel = distributionChannel;
    }
    public String getCode() {
        return code;
    }

}
