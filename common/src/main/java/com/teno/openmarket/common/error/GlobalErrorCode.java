package com.teno.openmarket.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    // SYSTEM (정산 및 시스템)
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", ErrorAction.DIALOG),
    SYSTEM_UPDATE_REQUIRED(HttpStatus.UPGRADE_REQUIRED, "새로운 버전이 출시되었습니다. 업데이트가 필요합니다.", ErrorAction.DIALOG), // 426
    SYSTEM_MAINTENANCE(HttpStatus.SERVICE_UNAVAILABLE, "현재 시스템 점검 중입니다.", ErrorAction.DIALOG),
    SYSTEM_INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", ErrorAction.TOAST),
    SYSTEM_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다.", ErrorAction.TOAST),
    SYSTEM_CIRCUIT_OPEN(HttpStatus.SERVICE_UNAVAILABLE, "접속이 지연되고 있습니다.", ErrorAction.DIALOG),
    SYSTEM_RESOURCE_CONFLICT(HttpStatus.CONFLICT, "다른 관리자에 의해 변경되었습니다.", ErrorAction.REFRESH),
    SYSTEM_BATCH_RUNNING(HttpStatus.CONFLICT, "현재 정산 작업이 진행 중입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "정산 데이터 오류가 발생했습니다.", ErrorAction.DIALOG),
    SYSTEM_FILE_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다.", ErrorAction.TOAST),
    SYSTEM_FILE_TYPE_ERROR(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_DUPLICATED(HttpStatus.CONFLICT, "이미 지급 요청된 정산 건입니다.", ErrorAction.TOAST),
    SYSTEM_SETTLEMENT_ZERO(HttpStatus.BAD_REQUEST, "지급할 정산 금액이 없습니다.", ErrorAction.TOAST),
    // [추가됨] (#20)
    SYSTEM_FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.", ErrorAction.DIALOG),

    // STORE (상점 및 상품)
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다.", ErrorAction.TOAST),
    SHOP_SUSPENDED(HttpStatus.FORBIDDEN, "운영 정책 위반으로 판매가 중지되었습니다.", ErrorAction.DIALOG),
    SHOP_ACCOUNT_EMPTY(HttpStatus.BAD_REQUEST, "정산 계좌 정보가 없습니다.", ErrorAction.TOAST),
    SHOP_ACCOUNT_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "예금주 정보가 일치하지 않습니다.", ErrorAction.DIALOG),
    SHOP_NOT_OWNER(HttpStatus.FORBIDDEN, "관리 권한이 없습니다.", ErrorAction.DIALOG),
    SHOP_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 상점명입니다.", ErrorAction.TOAST),
    SHOP_STATUS_INVALID(HttpStatus.BAD_REQUEST, "재신청 가능한 상태가 아닙니다.", ErrorAction.TOAST),
    SHOP_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "운영 중단된 상점입니다.", ErrorAction.DIALOG),
    SHOP_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 상점입니다.", ErrorAction.TOAST),
    SHOP_APPLICANT_INVALID(HttpStatus.BAD_REQUEST, "신청자 계정 상태를 확인해주세요.", ErrorAction.TOAST),
    SHOP_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.", ErrorAction.TOAST),
    SHOP_CATEGORY_NOT_LEAF(HttpStatus.BAD_REQUEST, "최하위 카테고리를 선택해주세요.", ErrorAction.TOAST),
    SHOP_IMAGE_LIMIT(HttpStatus.BAD_REQUEST, "이미지는 최대 10장까지 등록 가능합니다.", ErrorAction.TOAST),
    SHOP_PRODUCT_FROZEN(HttpStatus.CONFLICT, "진행 중인 타임딜이 있어 수정할 수 없습니다.", ErrorAction.DIALOG),
    // [추가됨] (#55) 유저가 이미 상점 개설을 신청했거나 운영 중인 경우
    SHOP_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 상점 개설을 신청했거나 운영 중입니다.", ErrorAction.DIALOG),

    // ORDER (주문 및 결제)
    ORDER_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "결제에 실패했습니다.", ErrorAction.DIALOG),
    ORDER_PG_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "결제 응답이 지연되고 있습니다.", ErrorAction.TOAST), // 504
    ORDER_IDEMPOTENCY_ERROR(HttpStatus.CONFLICT, "유효하지 않은 요청입니다.", ErrorAction.DIALOG),
    ORDER_STATE_CONFLICT(HttpStatus.CONFLICT, "이미 처리된 주문입니다.", ErrorAction.DIALOG),
    ORDER_COUPON_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않거나 적용할 수 없는 쿠폰입니다.", ErrorAction.TOAST),
    ORDER_REFUND_OVER(HttpStatus.BAD_REQUEST, "환불 가능 금액을 초과했습니다.", ErrorAction.DIALOG),
    ORDER_RISK_DETECTED(HttpStatus.FORBIDDEN, "이상 거래가 감지되었습니다.", ErrorAction.DIALOG),
    ORDER_PAYMENT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 정보가 올바르지 않습니다.", ErrorAction.DIALOG),
    ORDER_OWN_PRODUCT(HttpStatus.BAD_REQUEST, "본인 상점의 상품은 구매할 수 없습니다.", ErrorAction.DIALOG),

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
    DEAL_QUEUE_INVALID(HttpStatus.BAD_REQUEST, "비정상적인 접근입니다.", ErrorAction.DIALOG),

    // USER (회원 및 권한)
    USER_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 불일치합니다.", ErrorAction.TOAST),
    USER_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "로그인 정보가 만료되었습니다.", ErrorAction.REDIRECT),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", ErrorAction.DIALOG),
    USER_ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "계정이 잠금 처리되었습니다.", ErrorAction.DIALOG),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.", ErrorAction.REDIRECT),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.", ErrorAction.TOAST),
    USER_SUSPENDED(HttpStatus.FORBIDDEN, "이용이 정지된 계정입니다.", ErrorAction.DIALOG),
    USER_PASSWORD_SAME(HttpStatus.BAD_REQUEST, "현재 비밀번호와 동일하게 변경할 수 없습니다.", ErrorAction.TOAST),
    USER_ADDRESS_LIMIT(HttpStatus.BAD_REQUEST, "배송지는 최대 5개까지만 등록 가능합니다.", ErrorAction.TOAST),
    USER_ADDRESS_DEFAULT(HttpStatus.BAD_REQUEST, "기본 배송지는 삭제할 수 없습니다.", ErrorAction.TOAST),
    // [추가됨] (#52)
    USER_ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "배송지 정보를 찾을 수 없습니다.", ErrorAction.TOAST),
    USER_HAS_ACTIVE_SHOP(HttpStatus.BAD_REQUEST, "운영 중인 상점이 있어 탈퇴할 수 없습니다.", ErrorAction.DIALOG),
    // [추가됨] (#14)
    USER_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "필수 약관에 모두 동의해야 합니다.", ErrorAction.TOAST),
    // [추가됨] (#17)
    USER_LOGOUT(HttpStatus.UNAUTHORIZED, "로그아웃된 사용자입니다. 다시 로그인해주세요.", ErrorAction.REDIRECT),
    // [추가됨] (#17)
    USER_AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.", ErrorAction.REDIRECT);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
