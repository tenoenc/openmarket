package com.teno.openmarket.user.feature.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * [토큰 응답 DTO]
 * <p>
 * 로그인 성공 시 발급된 Access Token과 Refresh Token 정보를 담습니다.
 */
@Getter
@Builder
@Schema(name = "TokenResponse")
public class TokenResponse {

    /**
     * Access Token
     * <p>
     * API 요청 시 Authorization 헤더(Bearer)에 포함하여 전송해야 합니다.
     * 유효 기간이 짧습니다 (30분).
     */
    @Schema(example = "eyJhbGciOiJIUzI1NiIsIn...")
    private final String accessToken;

    /**
     * Refresh Token
     * <p>
     * Access Token 만료 시 재발급을 위해 사용됩니다.
     * 유효 기간이 깁니다 (14일).
     */
    @Schema(example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
    private final String refreshToken;

    /**
     * 토큰 타입
     * <p>
     * 일반적으로 "Bearer" 타입을 사용합니다.
     */
    @Schema(example = "Bearer")
    @Builder.Default
    private final String grantType = "Bearer";

    /**
     * Access Token 유효 시간 (ms)
     * <p>
     * 클라이언트 측에서 토큰 만료 시간을 계산하여, 만료 전 갱신을 요청하는 데 사용됩니다.
     */
    @Schema(example = "1800000")
    private final Long expiresIn;
}
