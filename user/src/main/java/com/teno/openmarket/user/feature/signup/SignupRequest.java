package com.teno.openmarket.user.feature.signup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * [회원가입 요청 DTO]
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SignupRequest")
public class SignupRequest {

    /**
     * 사용자 이메일 (로그인 ID)
     * <p>
     * 시스템 내에서 고유한 식별자로 사용됩니다.
     */
    @Schema(example = "user@example.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /**
     * 비밀번호
     * <p>
     * 영문, 숫자, 특수문자를 각각 1개 이상 포함하여 8~20자 이내로 입력해야 합니다.
     * DB 저장 시 Bcrypt로 암호화됩니다.
     */
    @Schema(example = "Password123!")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
    )
    private String password;

    /**
     * 사용자 설명
     */
    @Schema(example = "홍길동")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
    private String name;

    /**
     * 휴대전화 번호
     * <p>
     * 하이픈(-)을 포함한 형식만 허용됩니다.
     */
    @Schema(example = "010-1234-5678")
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;

    /**
     * 약관 동의 ID 목록
     * <p>
     * 사용자가 동의한 약관의 고유 ID(PK) 리스트입니다.
     * 필수 약관이 포함되어 있는지 여부는 비즈니스 로직(Service)에서 검증합니다.
     */
    @Schema(example = "[1, 2, 3]")
    @NotEmpty(message = "약관 동의 내역은 필수입니다.")
    private List<Long> termIds;
}
