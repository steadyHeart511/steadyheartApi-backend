package com.steadyheart.steadyheartinterface.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.alimt20181012.AsyncClient;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.sdk.service.alimt20181012.models.TranslateGeneralResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author lts
 * @create 2024-01-08 9:46
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.translation.client")
@Data
@ComponentScan
@Slf4j
public class TranslationConfig {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 配置能发送机器翻译请求的客户端
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Bean
    public AsyncClient asyncClient() throws ExecutionException, InterruptedException {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKey)
                .accessKeySecret(secretKey)
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("mt.cn-hangzhou.aliyuncs.com")
                )
                .build();
        return client;
    }

}
