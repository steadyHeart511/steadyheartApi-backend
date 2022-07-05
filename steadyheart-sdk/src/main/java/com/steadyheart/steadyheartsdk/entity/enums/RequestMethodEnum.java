package com.steadyheart.steadyheartsdk.entity.enums;

import lombok.Data;

/**
 * 请求方式
 * @author lts
 * @create 2023-11-02 14:28
 */
public enum RequestMethodEnum {

    GET("GET"),
    POST("POST");

    private String value;

    RequestMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
