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
    SYSTEM_RESOURCE_CONFLICT(HttpStatus.CONFLICT, "다른 관리자에 의해 변경되었습니다.", ErrorAction.REFRESH);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
