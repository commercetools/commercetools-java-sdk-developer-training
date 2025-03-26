package com.training.handson.dto;

import java.util.Map;

public class CustomObjectRequest {

    private String container;
    private String key;
    private Map<String, Object> jsonObject;

    public Map<String, Object> getJsonObject() {
        return jsonObject;
    }
    public String getKey() {
        return key;
    }
    public String getContainer() {
        return container;
    }
}
