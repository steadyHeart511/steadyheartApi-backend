package com.steadyheart.steadyheartsdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.steadyheart.steadyheartsdk.entity.User;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lts
 * @create 2023-10-11 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SteadyheartClient {

    private String accessKey;

    private String secretKey;

    public Map<String,String> headMap(String body) {
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("body",body);
        map.put("sign", SignUtils.sign(secretKey,body));
        map.put("nonce","1000");
        map.put("timestamp",String.valueOf(LocalDateTime.now()));
        return map;
    }

    public String getNameByPost (User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse response = HttpRequest.post("http://localhost:8083/name/user")
                .addHeaders(headMap(json))
                .body(json)
                .execute();
        return response.body();
    }

}
