package com.steadyheart.steadyheartinterface.utils;

import cn.hutool.http.HttpUtil;
import com.steadyheart.steadyheartsdk.entity.enums.ErrorCode;
import com.steadyheart.steadyheartsdk.exception.ApiException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author lts
 * @create 2024-01-07 16:38
 */
public class RequestUtil {

    /**
     * 将参数拼接到路径中
     * @param baseUrl   基础路径
     * @param params    参数对象
     * @param <T>   参数对象类型
     * @return  响应结果
     */
    private static <T> String buildUrl(String baseUrl, T params) {
        //  非空校验
        if (params == null) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "传入参数对象为null");
        }

        //  用于拼接完整带参Url
        StringBuilder url = new StringBuilder(baseUrl);
        Class<?> paramsClass = params.getClass();

        //  获取对象上的属性
        Field[] fields = paramsClass.getDeclaredFields();

        //  没属性就无需拼接，直接返回即可
        if (fields.length == 0) {
            return url.toString();
        //   有参数就需要拼接问号
        } else {
            url.append("?");
        }

        //  遍历属性并进行拼接
        //  比如 拼接前：http://www.baidu.com  -> 拼接后：http://www.baidu.com?a=1&b=2&c="xx"
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Object fieldValue = field.get(fieldName);
                url.append(fieldName).append("=").append(fieldValue).append("&");
            } catch (IllegalAccessException e) {
                throw new ApiException(ErrorCode.OPERATION_ERROR, "构建url异常");
            }
        }

        //拼接完成之后会多个&,需要去掉
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    /**
     * 发送get请求
     * @param url   请求地址
     * @param params    请求需要的参数
     * @param <T>   请求参数对象类型
     * @return  响应结果
     */
    public static <T> String get(String url,T params) {

        return get(buildUrl(url,params));
    }

    /**
     * 发送get请求
     * @param url   请求地址
     * @return  响应结果
     */
    public static String get(String url) {
        return HttpUtil.get(url);
    }
}
