package com.teno.openmarket.core.security.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", ErrorAction.DIALOG),
    SYSTEM_UPDATE_REQUIRED(HttpStatus.UPGRADE_REQUIRED, "새로운 버전이 출시되었습니다. 업데이트가 필요합니다.", ErrorAction.DIALOG), // 426
    SYSTEM_MAINTENANCE(HttpStatus.SERVICE_UNAVAILABLE, "현재 시스템 점검 중입니다.", ErrorAction.DIALOG),
    SYSTEM_INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", ErrorAction.TOAST),
    SYSTEM_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다.", ErrorAction.TOAST),
    SYSTEM_CIRCUIT_OPEN(HttpStatus.SERVICE_UNAVAILABLE, "접속이 지연되고 있습니다.", ErrorAction.DIALOG),
    SYSTEM_RESOURCE_CONFLICT(HttpStatus.CONFLICT, "다른 관리자에 의해 변경되었습니다.", ErrorAction.REFRESH),

    // TODO: 아래 에러 코드들은 common-security 모듈로 이동해야 함
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
