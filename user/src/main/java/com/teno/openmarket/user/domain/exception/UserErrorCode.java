package com.teno.openmarket.user.domain.exception;

import com.teno.openmarket.common.error.ErrorAction;
import com.teno.openmarket.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * [User 도메인 전용 에러 코드]
 * <p>
 * 로그인 실패, 회원 상태, 배송지 등 User 모듈의 비즈니스 규칙 위반 시 발생하는 오류를 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // USER (회원 및 권한)
    USER_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 불일치합니다.", ErrorAction.TOAST),
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
    USER_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "필수 약관에 모두 동의해야 합니다.", ErrorAction.TOAST);

    private final HttpStatus status;
    private final String message;
    private final ErrorAction action;
}
