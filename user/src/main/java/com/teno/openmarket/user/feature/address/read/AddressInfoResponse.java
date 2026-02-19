package com.teno.openmarket.user.feature.address.read;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * [배송지 상세 정보 응답 DTO]
 * <p>
 * 등록된 배송지의 상세 정보(목록 및 단건 조회)를 클라이언트에게 반환할 때 사용하는 데이터 객체입니다.
 */
@Getter
@Builder
@Schema(name = "AddressInfoResponse")
public class AddressInfoResponse {

    /**
     * 배송지 고유 식별자 (PK)
     */
    @Schema(example = "1")
    private Long id;

    /**
     * 배송지 별칭 (배송지명)
     * <p>
     * '집', '회사' 등 배송지를 쉽게 구분하기 위한 이름입니다.
     */
    @Schema(example = "집")
    private String addressName;

    /**
     * 수령인 이름
     * <p>
     * 실제로 물품을 수령할 사람의 실명입니다.
     */
    @Schema(example = "홍길동")
    private String recipientName;

    /**
     * 수령인 휴대전화 번호
     * <p>
     * 하이픈(-)을 포함한 휴대전화 번호입니다.
     */
    @Schema(example = "010-1234-5678")
    private String recipientPhone;

    /**
     * 우편번호
     * <p>
     * 5자리 숫자로 구성된 우편번호 체계입니다.
     */
    @Schema(example = "12345")
    private String zipCode;

    /**
     * 기본 주소
     * <p>
     * 도로명 주소 또는 지번 주소의 시/군/구 및 도로명 정보를 포함합니다.
     */
    @Schema(example = "서울특별시 강남구 테헤란로 123")
    private String addressBase;

    /**
     * 상세 주소
     * <p>
     * 아파트 동/호수, 건물 층수 등 기본 주소 이후의 상세 위치 정보입니다.
     */
    @Schema(example = "101동 202호")
    private String addressDetail;

    /**
     * 기본 배송지 여부
     * <p>
     * true일 경우 기본 배송지로 지정된 상태이며, 목록 조회 시 최상단에 노출됩니다.
     */
    @Schema(example = "true")
    private boolean isDefault;
}
