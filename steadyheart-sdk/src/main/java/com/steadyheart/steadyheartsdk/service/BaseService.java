package com.steadyheart.steadyheartsdk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import com.steadyheart.steadyheartsdk.entity.enums.ErrorCode;
import com.steadyheart.steadyheartsdk.entity.request.BaseRequest;
import com.steadyheart.steadyheartsdk.entity.response.BaseResponse;
import com.steadyheart.steadyheartsdk.entity.response.ErrorResponse;
import com.steadyheart.steadyheartsdk.entity.response.CurrencyResponse;
import com.steadyheart.steadyheartsdk.exception.ApiException;
import com.steadyheart.steadyheartsdk.utils.ResultUtils;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lts
 * @create 2023-11-02 14:35
 */
@Slf4j
@Data
public abstract class BaseService implements ApiService {

    private String GATEWAY = "http://localhost:9090/api";

    private SteadyheartClient steadyheartClient;

    @Override
    public <O, T> BaseResponse<T> request(BaseRequest<O, T> request) {
        return res(request);
    }


    @Override
    public <O, T> BaseResponse<T> request(SteadyheartClient steadyheartClient, BaseRequest<O, T> request) {
        checkClient(steadyheartClient);
        return request(request);
    }

    /**
     * 赋值steadyheartClient
     * @param steadyheartClient 客户端信息
     */
    public void checkClient(SteadyheartClient steadyheartClient) {
        //  配置文件和参数都没有提供ak、sk
        if (steadyheartClient == null && this.steadyheartClient == null) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "需要配置accessKey和secretKey");
        }
        //如果参数传入了，以参数传入的为主
        if (steadyheartClient != null && !StringUtils.isAnyBlank(steadyheartClient.getAccessKey(), steadyheartClient.getSecretKey())) {
            this.steadyheartClient = steadyheartClient;
        }
    }

    /**
     * 获取响应对象
     * 感觉这里用了模板方法
     *
     * @param request
     * @param <O>
     * @param <T>
     * @return
     */
    private <O, T> BaseResponse<T> res(BaseRequest<O, T> request) {
        if (steadyheartClient == null || request == null || StringUtils.isAnyBlank(steadyheartClient.getAccessKey(), steadyheartClient.getSecretKey())) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "需要配置accessKey和secretKey");
        }

        //  发送请求
        HttpResponse response = sendRequest(request);

        //  响应体数据
        String body = response.body();

        Map<String,Object> map = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
        }.getType());

        //  不为空表示的是由其他模块抛了异常，被异常处理器捕获了返回baseResponse，因此可以直接返回了
        if (map.get("code") != null) {
            // return JSONUtil.toBean(body, BaseResponse.class);
            return parseJsonToObject(body,BaseResponse.class);
        }

        //  获取请求所指定的响应类
        // Class<T> responseClass = request.getResponseClass();
        // T responseInstance = null;
        // try {
        //
        //     //  获取响应实例
        //     responseInstance = responseClass.newInstance();
        // } catch (Exception e) {
        //     throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        // }

        //  正常返回了
        T responseInstance = parseJsonToObject(body, request.getResponseClass());
        // T responseInstance = new Gson().fromJson(body, request.getResponseClass());
        // T responseInstance = JSONUtil.toBean(body, request.getResponseClass());
        // Map<String, Object> data = new HashMap<>();

        //  响应失败
        // if (response.getStatus() != 200) {
        //
        //     //  将json转为对象
        //     ErrorResponse errorResponse = JSONUtil.toBean(body,ErrorResponse.class);
        //
        //     //  返回错误信息
        //     data.put("errorMessage", errorResponse.getMessage());
        //     data.put("code", errorResponse.getCode());
        // } else {
        //     try {
        //         //  尝试解析为map
        //         data = new Gson().fromJson(body,new TypeToken<Map<String,Object>>(){}.getType());
        //     }
        //     catch (JsonSyntaxException e) {
        //
        //         //  json转换失败，直接不转了，直接和value凑成一对
        //         data.put("value",body);
        //     }
        // }
        // responseInstance.setData(data);
        return ResultUtils.success(responseInstance);
    }

    private <T> T parseJsonToObject(String body,Class<T> clazz)  {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e ) {
            log.info("反射创建对象失败");
            throw new ApiException(ErrorCode.SYSTEM_ERROR);
        }
        // if (instance instanceof BaseResponse) {
        //     instance = (T) JSONUtil.toBean(body, BaseResponse.class);
        //     return instance;
        // }
        if (instance instanceof CurrencyResponse) {
            Map<String, Object> map = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
            }.getType());
            CurrencyResponse currencyResponse = (CurrencyResponse) instance;
            currencyResponse.setData(map);
            return (T) currencyResponse;
        } else {
            instance = JSONUtil.toBean(body, clazz);
            return instance;
        }
    }

    /**
     * 发送请求
     *
     * @param request 请求参数
     * @param <O>     参数类型
     * @param <T>     响应类型
     * @return 响应对象
     */
    private <O, T> HttpResponse sendRequest(BaseRequest<O, T> request) {

        //  根据request需要的方法类型，获取带有指定请求方式的HttpRequest
        HttpRequest httpRequest = getHttpRequestByMethod(request);

        //  执行
        try (HttpResponse response = httpRequest.execute()) {
            return response;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 根据请求方式来发送不同的请求
     *
     * @param request 请求信息
     * @param <O>     信息类型
     * @param <T>     响应类型
     * @return HttpRequest
     */
    private <O, T> HttpRequest getHttpRequestByMethod(BaseRequest<O, T> request) {
        String method = request.getMethod().toUpperCase();
        String path = request.getPath();
        //  如果路径中有网关信息，则去掉
        if (path.startsWith(GATEWAY)) {
            path = path.substring(GATEWAY.length());
        }
        if (StringUtils.isBlank(method)) {
            throw new ApiException(ErrorCode.PARAMS_ERROR, "请填写请求方式");
        }
        if (StringUtils.isBlank(path)) {
            throw new ApiException(ErrorCode.PARAMS_ERROR, "请填写请求路径");
        }
        log.info("请求方法：{}，请求路径：{}，请求参数：{}", method, path, request.getRequestParams());
        //  拼接请求路径
        String url = GATEWAY + path;
        HttpRequest httpRequest;
        //  判断method类型
        switch (method) {
            case "GET":
                httpRequest = HttpRequest.get(splicingGetRequest(request, url));
                break;
            case "POST":
                httpRequest = HttpRequest.post(url);
                break;
            default:
                throw new ApiException(ErrorCode.PARAMS_ERROR, "暂不支持该请求方式");
        }
        Map<String, String> headers = getHeaders(JSONUtil.toJsonStr(request), steadyheartClient);
        return httpRequest.addHeaders(headers).body(JSONUtil.toJsonStr(request.getRequestParams()));
    }

    /**
     * get发送请求时拼接参数到url中
     *
     * @param request 请求信息
     * @param url     请求路径
     * @param <O>     参数类型
     * @param <T>     响应类型
     * @return 拼接好的路径
     */
    private <O, T> String splicingGetRequest(BaseRequest<O, T> request, String url) {
        StringBuilder stringBuilder = new StringBuilder(url);
        Map<String, Object> requestParams = request.getRequestParams();
        //  判断请求参数是否存在
        if (!requestParams.isEmpty()) {
            stringBuilder.append("?");
            requestParams.entrySet()
                    .stream()
                    .forEach(param -> stringBuilder.append(param.getKey()).append("=").append(param.getValue()).append("&"));
            //  去掉多余的&
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        log.info("GET请求拼接后的完整路径：{}",stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * 发送请求前获取所需要封装的请求头
     *
     * @param body              request内容
     * @param steadyheartClient 客户端sdk
     * @return 请求头
     */
    private Map<String, String> getHeaders(String body, SteadyheartClient steadyheartClient) {
        Map<String, String> headers = new HashMap<>();
        headers.put("accessKey", steadyheartClient.getAccessKey());
        headers.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String encryptBody = SecureUtil.md5(body);
        headers.put("body", encryptBody);
        headers.put("sign", SignUtils.sign(steadyheartClient.getSecretKey(), encryptBody));
        return headers;
    }

}
