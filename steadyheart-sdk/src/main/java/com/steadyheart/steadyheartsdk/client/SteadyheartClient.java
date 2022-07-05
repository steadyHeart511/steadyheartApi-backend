package com.steadyheart.steadyheartsdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
