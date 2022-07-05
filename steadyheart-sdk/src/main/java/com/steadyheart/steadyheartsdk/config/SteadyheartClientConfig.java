package com.steadyheart.steadyheartsdk.config;

import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import com.steadyheart.steadyheartsdk.service.ApiService;
import com.steadyheart.steadyheartsdk.service.impl.ApiServiceImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lts
 * @create 2023-10-11 14:18
 */
@Configuration
@ConfigurationProperties("steadyheart.client")
@ComponentScan
@Data
public class SteadyheartClientConfig {

    private String accessKey;

    private String secretKey;

    /**
     * 网关
     */
    private String host;

    @Bean
    public SteadyheartClient steadyheartClient() {
        return new SteadyheartClient(accessKey,secretKey);
    }

    @Bean
    public ApiService apiService() {
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setSteadyheartClient(new SteadyheartClient(accessKey,secretKey));
        if (StringUtils.isNotBlank(host)) {
            apiService.setGATEWAY(host);
        }
        return apiService;
    }


}
