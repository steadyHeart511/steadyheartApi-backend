package com.steadyheart.steadyheartgateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steadyheart.steadyheartcommon.service.common.BaseResponse;
import com.steadyheart.steadyheartcommon.service.common.ResultUtils;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * 全局异常处理器
 * @author lts
 * @create 2023-11-03 21:58
 */
@Slf4j
@Configuration
@Order(-1)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对网关处理出现的异常进行处理
     * @param exchange
     * @param ex
     * @return
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        //  设置返回数据格式
        headers.setContentType(MediaType.APPLICATION_JSON);
        //  数据进行响应则报错（盲猜这个时候可能网络断开导致数据无法发送出去，因此报异常被处理器捕获）
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // response.setStatusCode(HttpStatus.FORBIDDEN);
        //  返回错误信息
        BaseResponse<Object> error = null;
        if (ex instanceof BusinessException) {
            error = ResultUtils.error(((BusinessException) ex).getCode(), ex.getMessage());
        } else {
            error = ResultUtils.error(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        }
        log.info("全局异常处理器捕获了异常，异常信息为{}",error);
        DataBufferFactory factory = response.bufferFactory();

        try {
            byte[] errorBytes = objectMapper.writeValueAsBytes(error);
            DataBuffer wrap = factory.wrap(errorBytes);
            return response.writeWith(Mono.just(wrap));
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常：{}", e.getMessage());
            return Mono.error(e);
        }
    }
}
