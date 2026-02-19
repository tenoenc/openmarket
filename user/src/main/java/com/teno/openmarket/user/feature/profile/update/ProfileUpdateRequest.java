package com.teno.openmarket.user.feature.profile.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [내 정보 수정 요청 DTO]
 * <p>
 * 사용자가 자신의 이름과 전화번호를 수정할 때 사용하는 객체입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProfileUpdateRequest")
public class ProfileUpdateRequest {

    /**
     * 사용자 이름
     * <p>
     * 회원가입 시와 동일하게 2~10자 길이 제한을 가집니다.
     */
    @Schema(example = "테스터(수정)")
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
}