package com.teno.openmarket.user.feature.reissue;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [토큰 재발급 요청 DTO]
 * <p>
 * Access Token 만료 시, 유효한 Refresh Token을 통해 인증을 갱신하기 위한 요청 객체입니다.
 * 전달받은 Refresh Token은 1회 사용 후 즉시 폐기되며, 새로운 토큰 쌍이 발급됩니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReissueRequest")
public class ReissueRequest {

    /**
     * Refresh Token
     * <p>
     * 이전에 발급받은 리프레시 토큰입니다.
     * Bearer 접두어 없이 토큰 문자열 그대로 전송해야 합니다.
     */
    @Schema(example = "eyJhbGciOiJIUzI1NiIsIn...")
    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;
}
