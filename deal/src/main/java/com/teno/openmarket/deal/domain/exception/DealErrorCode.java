package com.teno.openmarket.deal.domain.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DealErrorCode implements ErrorCode {

    // DEAL (타임딜 및 재고)
    DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 딜입니다.", ErrorAction.TOAST),
    DEAL_NOT_OPEN(HttpStatus.BAD_REQUEST, "아직 판매 시작 전입니다.", ErrorAction.DIALOG),
    DEAL_CLOSED(HttpStatus.BAD_REQUEST, "판매가 종료된 딜입니다.",ErrorAction.DIALOG),
    DEAL_OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 모두 소진되었습니다.", ErrorAction.DIALOG),
    DEAL_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "1인당 구매 가능 수량을 초과했습니다.", ErrorAction.DIALOG),
    DEAL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 타임딜이 등록된 상품입니다.", ErrorAction.DIALOG),
    DEAL_PRICE_INVALID(HttpStatus.BAD_REQUEST, "할인가격 설정을 확인해주세요.", ErrorAction.TOAST),
    DEAL_TIME_INVALID(HttpStatus.BAD_REQUEST, "시간 설정을 확인해주세요.", ErrorAction.TOAST),
    DEAL_STATUS_CANNOT_MODIFY(HttpStatus.CONFLICT, "진행 중이거나 종료된 딜은 수정할 수 없습니다.", ErrorAction.TOAST),
    DEAL_QUEUE_EXPIRED(HttpStatus.BAD_REQUEST, "대기열 유효 시간이 만료되었습니다. 다시 시도해주세요.", ErrorAction.DIALOG),
    DEAL_QUEUE_INVALID(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다.", ErrorAction.DIALOG);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
