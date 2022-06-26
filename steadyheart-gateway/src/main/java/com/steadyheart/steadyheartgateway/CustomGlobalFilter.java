package com.steadyheart.steadyheartgateway;

import cn.hutool.core.collection.CollUtil;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lts
 * @create 2023-10-23 10:44
 */
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    //  黑名单
    public static final List<String> IP_BLACK_LIST = new ArrayList<>();

    /**
     *
     * @param exchange  可以获取请求对象和响应对象，还提供了一些拓展方法
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 打印请求日志
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getId();
        RequestPath path = request.getPath();
        String address = request.getRemoteAddress().getAddress().getHostAddress();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("request start，id: {}, path: {}, ip: {}, params: {}", id, path,
                address, queryParams);
        // 2. 先进行访问控制，看ip是否合法
        ServerHttpResponse response = exchange.getResponse();
        if (IP_BLACK_LIST.contains(address)) {
            return handleNoAuth(response);
        }
        // 3. 鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");

        if (!"lts".equals(accessKey)) {
            return handleNoAuth(response);
        }

        //  todo 需要自己写逻辑
        if (Integer.valueOf(nonce) > 10000) {
            return handleNoAuth(response);
        }

//        5分钟之后过期
        Long currentTime = System.currentTimeMillis();
        final Long EXPIRE_TIME = 5 * 60L;
       if ((currentTime - Long.parseLong(timestamp)) / 1000 >= EXPIRE_TIME) {
           return handleNoAuth(response);
       }
        if (!SignUtils.sign("xx",body).equals(sign)) {
            return handleNoAuth(response);
        }
        // 4. 看接口是否存在
        // 5. 接口调用
        Mono<Void> filter = chain.filter(exchange);
        return handleResponse(exchange,chain);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 包装response，使得接口服务调用完之后进行统一操作
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange,GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                // 打印日志
                                log.info("响应状态：{}，响应结果：{}",originalResponse.getStatusCode(),data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据(正常返回)
        }catch (Exception e){
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

}
