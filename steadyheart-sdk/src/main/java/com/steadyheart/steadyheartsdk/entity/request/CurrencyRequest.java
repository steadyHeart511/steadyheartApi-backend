package com.steadyheart.steadyheartsdk.entity.request;

import com.steadyheart.steadyheartsdk.entity.response.CurrencyResponse;

/**
 * 通用请求
 * @author lts
 * @create 2023-11-03 9:19
 */
public class CurrencyRequest extends BaseRequest<Object, CurrencyResponse>{

    private String method;
    private String path;

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Class<CurrencyResponse> getResponseClass() {
        return CurrencyResponse.class;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
