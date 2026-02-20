package com.teno.openmarket.core.security.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {

    SECURITY_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "로그인 정보가 만료되었습니다.", ErrorAction.REDIRECT),
    SECURITY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", ErrorAction.DIALOG),
    // [추가됨] (#17)
    SECURITY_LOGOUT(HttpStatus.UNAUTHORIZED, "로그아웃된 사용자입니다. 다시 로그인해주세요.", ErrorAction.REDIRECT),
    // [추가됨] (#17)
    SECURITY_AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.", ErrorAction.REDIRECT);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
