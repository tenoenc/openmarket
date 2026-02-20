package com.teno.openmarket.system.domain.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    // SYSTEM (정산 및 시스템)
    SYSTEM_BATCH_RUNNING(HttpStatus.CONFLICT, "현재 정산 작업이 진행 중입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "정산 데이터 오류가 발생했습니다.", ErrorAction.DIALOG),
    SYSTEM_FILE_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다.", ErrorAction.TOAST),
    SYSTEM_FILE_TYPE_ERROR(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_DUPLICATED(HttpStatus.CONFLICT, "이미 지급 요청된 정산 건입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_ZERO(HttpStatus.BAD_REQUEST, "지급할 정산 금액이 없습니다.", ErrorAction.TOAST),
    // [추가됨] (#20)
    SYSTEM_FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.", ErrorAction.DIALOG);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
