package com.steadyheart.steadyheartsdk.exception;

import com.steadyheart.steadyheartsdk.entity.enums.ErrorCode;
import com.steadyheart.steadyheartsdk.entity.response.BaseResponse;
import com.steadyheart.steadyheartsdk.entity.response.ErrorResponse;
import com.steadyheart.steadyheartsdk.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lts
 * @create 2024-01-08 22:42
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public BaseResponse handleApiException(ApiException e) {
        log.info("GlobalExceptionHandler[handleApiException] 异常信息：{}",e.getMessage());
        return ResultUtils.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return new ErrorResponse( ErrorCode.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
