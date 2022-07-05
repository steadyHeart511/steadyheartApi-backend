package com.steadyheart.steadyheartsdk.entity.request;

import com.steadyheart.steadyheartsdk.entity.enums.RequestMethodEnum;
import com.steadyheart.steadyheartsdk.entity.params.NameParams;
import com.steadyheart.steadyheartsdk.entity.response.NameResponse;

/**
 * @author lts
 * @create 2023-11-02 14:16
 */
public class NameRequest extends BaseRequest<NameParams, NameResponse>{

    @Override
    public String getPath() {
        return "/name";
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }

    @Override
    public Class<NameResponse> getResponseClass() {
        return NameResponse.class;
    }
}
