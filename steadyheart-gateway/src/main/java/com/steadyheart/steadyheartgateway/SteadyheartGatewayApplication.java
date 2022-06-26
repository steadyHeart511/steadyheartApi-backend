package com.steadyheart.steadyheartgateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDubbo
public class SteadyheartGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(SteadyheartGatewayApplication.class, args);
    }

    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }

}
