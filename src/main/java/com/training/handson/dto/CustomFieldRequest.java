package com.training.handson.dto;

public class CustomFieldRequest {

    private String customerId;
    private String orderNumber;
    private String addressKey;
    private String instructions;
    private String time;
    private boolean save;

    public String getCustomerId() {
        return customerId;
    }
    public String getInstructions() {
        return instructions;
    }
    public String getTime() {
        return time;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public String getAddressKey() {return addressKey;}
    public void setAddressKey(String addressKey) {this.addressKey = addressKey;}
    public boolean isSave() {
        return save;
    }
}
