package com.teno.openmarket.core.security.exception;

import com.teno.openmarket.core.security.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 디버깅용
    public BusinessException(ErrorCode errorCode, String debugMessage) {
        super(debugMessage);
        this.errorCode = errorCode;
    }
}
