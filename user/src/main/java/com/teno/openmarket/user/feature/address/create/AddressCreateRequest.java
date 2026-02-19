package com.teno.openmarket.user.feature.address.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [배송지 등록 요청 DTO]
 * <p>
 * 사용자가 새로운 배송지를 등록할 때 전달하는 데이터 객체입니다.
 * 최대 5개까지 등록 가능하며, 최초 등록 시 자동으로 기본 배송지로 설정됩니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressCreateRequest")
public class AddressCreateRequest {

    /**
     * 배송지 별칭 (배송지명)
     * <p>
     * '집', '회사' 등 배송지를 쉽게 구분하기 위한 선택적 입력값입니다.
     */
    @Schema(example = "집")
    @Size(max = 50, message = "배송지명은 50자 이하로 입력해주세요.")
    private String addressName;

    /**
     * 수령인 이름
     * <p>
     * 실제로 물품을 수령할 사람의 실명입니다.
     */
    @Schema(example = "홍길동")
    @NotBlank(message = "수령인 이름은 필수 입력값입니다.")
    private String recipientName;

    /**
     * 수령인 휴대전화 번호
     * <p>
     * 하이픈(-)을 포함한 올바른 휴대전화 번호 형식이어야 합니다.
     */
    @Schema(example = "010-1234-5678")
    @NotBlank(message = "수령인 전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String recipientPhone;

    /**
     * 우편번호
     * <p>
     * 5자리 숫자로 구성된 우편번호 체계를 사용합니다.
     */
    @Schema(example = "12345")
    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    private String zipCode;

    /**
     * 기본 주소
     * <p>
     * 도로명 주소 또는 지번 주소의 시/군/구 및 도로명 정보를 포함합니다.
     */
    @Schema(example = "서울특별시 강남구 테헤란로 123")
    @NotBlank(message = "기본 주소는 필수 입력값입니다.")
    private String addressBase;

    /**
     * 상세 주소
     * <p>
     * 아파트 동/호수, 건물 층수 등 기본 주소 이후의 상세 위치 정보입니다.
     */
    @Schema(example = "101동 202호")
    @NotBlank(message = "상세 주소는 필수 입력값입니다.")
    private String addressDetail;
}
