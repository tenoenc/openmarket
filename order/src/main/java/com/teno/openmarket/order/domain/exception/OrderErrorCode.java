package com.teno.openmarket.order.domain.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    // ORDER (주문 및 결제)
    ORDER_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "결제에 실패했습니다.", ErrorAction.DIALOG),
    ORDER_PG_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "결제 응답이 지연되고 있습니다.", ErrorAction.TOAST), // 504
    ORDER_IDEMPOTENCY_ERROR(HttpStatus.CONFLICT, "유효하지 않은 요청입니다.", ErrorAction.DIALOG),
    ORDER_STATE_CONFLICT(HttpStatus.CONFLICT, "이미 처리된 주문입니다.", ErrorAction.DIALOG),
    ORDER_COUPON_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않거나 적용할 수 없는 쿠폰입니다.", ErrorAction.TOAST),
    ORDER_REFUND_OVER(HttpStatus.BAD_REQUEST, "환불 가능 금액을 초과했습니다.", ErrorAction.DIALOG),
    ORDER_RISK_DETECTED(HttpStatus.FORBIDDEN, "이상 거래가 감지되었습니다.", ErrorAction.DIALOG),
    ORDER_PAYMENT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 정보가 올바르지 않습니다.", ErrorAction.DIALOG),
    ORDER_OWN_PRODUCT(HttpStatus.BAD_REQUEST, "본인 상점의 상품은 구매할 수 없습니다.", ErrorAction.DIALOG);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
