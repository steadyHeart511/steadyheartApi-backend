package com.steadyheart.steadyheartgateway;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.steadyheart.steadyheartcommon.service.common.ErrorCode;
import com.steadyheart.steadyheartcommon.exception.BusinessException;
import com.steadyheart.steadyheartcommon.model.entity.InterfaceInfo;
import com.steadyheart.steadyheartcommon.model.entity.User;
import com.steadyheart.steadyheartcommon.model.enums.InterfaceInfoEnum;
import com.steadyheart.steadyheartcommon.service.InnerInterfaceInfoService;
import com.steadyheart.steadyheartcommon.service.InnerUserInterfaceInfoService;
import com.steadyheart.steadyheartcommon.service.InnerUserService;
import com.steadyheart.steadyheartgateway.entity.RequestParamsField;
import com.steadyheart.steadyheartsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.steadyheart.steadyheartcommon.model.enums.UserAccountStatusEnum.BAN;

/**
 * @author lts
 * @create 2023-10-23 10:44
 */
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService userInterfaceInfoService;

    @DubboReference
    private InnerUserService userService;

    //  黑名单
    public static final List<String> IP_BLACK_LIST = new ArrayList<>();

    private static final String INTERFACE_HOST = "http://localhost:8083";

    /**
     * @param exchange 可以获取请求对象和响应对象，还提供了一些拓展方法
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 打印请求日志
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getId();
        String method = request.getMethodValue();
        String path = INTERFACE_HOST + request.getPath().value().replace("/api", "");
        String address = request.getRemoteAddress().getAddress().getHostAddress();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("request start，id: {}, path: {}, metthod: {},ip: {}, params: {}", id, path, method,
                address, queryParams);
        return verifyParameter(exchange, chain);
    }

    /**
     * 校验参数,合法就放行
     *
     * @param exchange 拿到请求/响应对象
     * @param chain    用来放行请求
     * @return Mono是个异步类似promise
     */
    private Mono<Void> verifyParameter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String address = request.getRemoteAddress().getAddress().getHostAddress();
        // 先进行访问控制，看ip是否合法
        ServerHttpResponse response = exchange.getResponse();
        if (IP_BLACK_LIST.contains(address)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "IP已被封禁");
        }
        // 鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        // 请求头中参数必须完整
        if (StringUtils.isAnyBlank(body, sign, accessKey, timestamp)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        //        5分钟之后过期
        Long currentTime = System.currentTimeMillis() / 1000;
        final Long EXPIRE_TIME = 5 * 60L;
        if ((currentTime - Long.parseLong(timestamp)) >= EXPIRE_TIME) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "会话已过期,请重试！");
        }

        User user = null;
        try {
            user = userService.getInvokeUser(accessKey);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请填写accessKey");
        }
        if (user == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "accessKey填写有误");
        }
        //  用户被封号
        if (user.getStatus().equals(BAN)) {
            throw new BusinessException(ErrorCode.PROHIBITED, "账号已被封禁");
        }
        String secretKey = user.getSecretKey();
        if (!SignUtils.sign(secretKey, body).equals(sign)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "非法请求");
        }
        String method = request.getMethodValue();
        String uri = request.getURI().toString();
        if (StringUtils.isAnyBlank(method, uri)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数有误");
        }
        // 看接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(uri, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        //  接口下线
        if (InterfaceInfoEnum.OFFLINE.getValue().equals(interfaceInfo.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口已关闭");
        }
        //  请求参数校验，是否必须
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String requestParams = interfaceInfo.getRequestParams();

        //  有请求参数时才需要进行校验
        if(StringUtils.isNotBlank(requestParams)) {
            List<RequestParamsField> requestParamsFields = new Gson().fromJson(requestParams, new TypeToken<List<RequestParamsField>>() {
            }.getType());
            requestParamsFields.stream().forEach(requestParamsField -> {
                //  如果参数是必要的，就判断请求携带的参数是否有该参数
                if ("是".equals(requestParamsField.getRequired())) {
                    //  请求参数没有必要参数
                    if (StringUtils.isBlank(queryParams.getFirst(requestParamsField.getFieldName()))) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数" + requestParamsField.getFieldName() + "为空");
                    }
                }
            });
        }
        // 放行、处理响应
        return handleResponse(exchange, chain, user, interfaceInfo);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 包装response，使得接口服务调用完之后进行统一操作
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, User user, InterfaceInfo interfaceInfo) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //  调用成功，调用次数加一
                                try {
                                    userInterfaceInfoService.invokeCount(interfaceInfo.getId(), user.getId());
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"调用次数不足，请充值");
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                // 打印日志
                                log.info("响应状态：{}，响应结果：{}", originalResponse.getStatusCode(), data);
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
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

}
