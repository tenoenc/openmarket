package com.teno.openmarket.user.feature.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [로그인 요청 DTO]
 * <p>
 * 클라이언트로부터 전달받은 로그인 정보를 담는 객체입니다.
 * 입력값에 대한 기본적인 유효성 검증(Validation)을 수행합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginRequest")
public class LoginRequest {

    /**
     * 사용자 이메일 (로그인 ID)
     */
    @Schema(example = "user@example.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 비밀번호
     * <p>
     * 암호화되지 않은 평문(Plain Text) 비밀번호입니다.
     */
    @Schema(example = "Password123!")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
