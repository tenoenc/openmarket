package com.teno.openmarket.user.feature.profile.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * [내 정보 수정 응답 DTO]
 * <p>
 * 현재 로그인한 사용자의 프로필 정보를 담아 클라이언트에 반환하는 객체입니다.
 * 보안상 비밀번호 등 민감한 정보는 제외하고 응답합니다.
 */
@Getter
@Builder
@Schema(name = "ProfileUpdateResponse")
public class ProfileUpdateResponse {

    /**
     * 유저 식별자
     * <p>
     * 시스템 내부에서 회원을 고유하게 식별하는 ID (PK)입니다.
     */
    @Schema(example = "1")
    private Long id;

    /**
     * 이메일 주소
     * <p>
     * 로그인 시 사용되는 계정 ID (Email)입니다.
     */
    @Schema(example = "user@example.com")
    private String email;

    /**
     * 회원 실명
     */
    @Schema(example = "홍길동")
    private String name;

    /**
     * 전화번호
     * <p>
     * 하이픈(-)이 포함된 회원의 연락처 형식입니다.
     */
    @Schema(example = "010-1234-5678")
    private String phone;
}
