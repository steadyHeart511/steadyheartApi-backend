package com.steadyheart.steadyheartsdk.entity.request;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.steadyheart.steadyheartsdk.entity.response.CurrencyResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lts
 * @create 2023-11-02 10:00
 */
@Data
public abstract class BaseRequest<O,T> {

    /**
     * 接收前端传递的请求参数
     */
    private Map<String,Object> requestParams = new HashMap<>();

    /**
     * 获取请求接口路径
     * @return
     */
    public abstract String getPath();

    /**
     * 获取请求接口时的请求方式
     * @return
     */
    public abstract String getMethod();

    /**
     * 获取响应类
     * @return
     */
    public abstract Class<T> getResponseClass();

    public void setRequestParams(O params) {
        this.requestParams = new Gson().fromJson(JSONUtil.toJsonStr(params), new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}
