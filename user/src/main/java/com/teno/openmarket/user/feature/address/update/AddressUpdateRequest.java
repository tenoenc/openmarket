package com.teno.openmarket.user.feature.address.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressUpdateRequest")
public class AddressUpdateRequest {

    @Schema(example = "회사")
    @Size(max = 50, message = "배송지명은 50자 이하로 입력해주세요.")
    private String addressName;

    @Schema(example = "홍길동")
    @NotBlank(message = "수령인 이름은 필수 입력값입니다.")
    private String recipientName;

    @Schema(example = "010-1234-5678")
    @NotBlank(message = "수령인 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String recipientPhone;

    @Schema(example = "12345")
    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String zipCode;

    @Schema(example = "서울특별시 강남구 테헤란로 123")
    @NotBlank(message = "기본 주소는 필수 입력값입니다.")
    private String addressBase;

    @Schema(example = "101동 202호")
    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String addressDetail;
}
