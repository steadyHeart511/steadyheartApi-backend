package com.steadyheart.steadyheartsdk.exception;

import com.steadyheart.steadyheartsdk.entity.enums.ErrorCode;

/**
 * @author lts
 * @create 2023-11-02 14:48
 */
public class ApiException extends RuntimeException{

    private int code;

    public ApiException(int code,String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
