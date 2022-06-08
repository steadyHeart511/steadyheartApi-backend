package com.steadyheart.steadyheartsdk.config;

import com.steadyheart.steadyheartsdk.client.SteadyheartClient;
import lombok.Data;
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

    @Bean
    public SteadyheartClient steadyheartClient() {
        return new SteadyheartClient(accessKey,secretKey);
    }


}
