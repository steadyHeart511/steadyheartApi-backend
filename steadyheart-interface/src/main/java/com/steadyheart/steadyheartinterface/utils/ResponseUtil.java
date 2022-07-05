package com.steadyheart.steadyheartinterface.utils;

import com.alibaba.nacos.shaded.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.steadyheart.steadyheartsdk.entity.response.CurrencyResponse;

import java.util.Map;
import static com.steadyheart.steadyheartinterface.utils.RequestUtil.get;

/**
 * @author lts
 * @create 2024-01-07 16:38
 */
public class ResponseUtil {

    /**
     * 将json转为map
     *
     * @param response json数据
     * @return map对象
     */
    public static Map<String, Object> responseToMap(String response) {
        return new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 发送请求，获取响应结果
     * @param baseUrl
     * @param params
     * @param <T>
     * @return
     */
    // public static <T> CurrencyResponse baseResponse(String baseUrl, T params) {
    //     String result = get(baseUrl, params);
    //     Map<String, Object> map = responseToMap(result);
    //     CurrencyResponse currencyResponse = new CurrencyResponse();
    //     currencyResponse.setData(map);
    //     return currencyResponse;
    // }
}
