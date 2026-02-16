package com.teno.openmarket.user.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [사용자 권한 등급]
 * <p>
 * 시스템 내 사용자의 인가를 관리하는 권한 등급 클래스입니다.
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    /**
     * 일반 회원 권한
     * <p>
     * 판매자 입점 신청을 통해 권한 상승이 가능합니다.
     */
    ROLE_USER("일반 사용자"),

    /**
     * 판매자 권한
     * <p>
     * 관리자의 입점 승인 절차를 거쳐 부여되는 권한입니다.
     */
    ROLE_SELLER("판매자"),

    /**
     * 시스템 관리자 권한
     * <p>
     * 판매자 입점 신청에 대한 승인/반려 처리 권한을 가집니다.
     * 시스템 운영 정책 관리 및 전체 주문 현황 모니터링 등의 전역적인 제어가 가능합니다.
     */
    ROLE_ADMIN("관리자");

    private final String description;
}
