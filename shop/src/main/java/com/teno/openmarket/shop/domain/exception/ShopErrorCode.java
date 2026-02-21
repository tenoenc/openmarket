package com.teno.openmarket.shop.domain.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShopErrorCode implements ErrorCode {

    // SHOP (상점 및 상품)
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다.", ErrorAction.TOAST),
    SHOP_SUSPENDED(HttpStatus.FORBIDDEN, "운영 정책 위반으로 판매가 중지되었습니다.", ErrorAction.DIALOG),
    SHOP_ACCOUNT_EMPTY(HttpStatus.BAD_REQUEST, "정산 계좌 정보가 없습니다.", ErrorAction.TOAST),
    SHOP_ACCOUNT_VERIFY_FAILED(HttpStatus.BAD_REQUEST, "예금주 정보가 일치하지 않습니다.", ErrorAction.DIALOG),
    SHOP_NOT_OWNER(HttpStatus.FORBIDDEN, "관리 권한이 없습니다.", ErrorAction.DIALOG),
    SHOP_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 상점명입니다.", ErrorAction.TOAST),
    SHOP_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "운영 중단된 상점입니다.", ErrorAction.DIALOG),
    SHOP_ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 상점입니다.", ErrorAction.TOAST),
    SHOP_APPLICANT_INVALID(HttpStatus.BAD_REQUEST, "신청자 계정 상태를 확인해주세요.", ErrorAction.TOAST),
    SHOP_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.", ErrorAction.TOAST),
    SHOP_CATEGORY_NOT_LEAF(HttpStatus.BAD_REQUEST, "최하위 카테고리를 선택해주세요.", ErrorAction.TOAST),
    SHOP_IMAGE_LIMIT(HttpStatus.BAD_REQUEST, "이미지는 최대 10장까지 등록 가능합니다.", ErrorAction.TOAST),
    SHOP_PRODUCT_FROZEN(HttpStatus.CONFLICT, "진행 중인 타임딜이 있어 수정할 수 없습니다.", ErrorAction.DIALOG),
    // [추가됨] (#55) 유저가 이미 상점 개설을 신청했거나 운영 중인 경우
    SHOP_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 상점 개설을 신청했거나 운영 중입니다.", ErrorAction.DIALOG),
    // [SHOP_INVALID_STATUS 삭제됨] & [추가됨] (#56)
    SHOP_NOT_WAITING_STATUS(HttpStatus.BAD_REQUEST, "대기 상태의 상점만 승인 또는 반려 처리가 가능합니다.", ErrorAction.TOAST);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
