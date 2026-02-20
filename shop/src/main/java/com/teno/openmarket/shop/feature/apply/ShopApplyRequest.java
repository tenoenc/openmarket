package com.teno.openmarket.shop.feature.apply;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [상점 입점 신청 요청 DTO]
 * <p>
 * 일반 회원이 판매자로 활동하기 위해 상점 개설을 신청할 때 전달받는 데이터 객체입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ShopApplicationRequest")
public class ShopApplyRequest {

    /**
     * 신청할 상점 이름 (상점명)
     * <p>
     * 오픈마켓 내에서 고유하게 사용되는 상점의 이름입니다.
     */
    @Schema(example = "오픈마켓")
    @NotBlank(message = "상점명은 필수 입력값입니다.")
    @Size(max = 100, message = "상점명은 100자 이하로 입력해주세요.")
    private String shopName;

    /**
     * 사업자 등록번호
     * <p>
     * 상점 개설을 위한 필수 법적 식별 정보입니다.
     */
    @Schema(example = "123-45-67890")
    @NotBlank(message = "사업자 등록번호는 필수 입력값입니다.")
    @Size(max = 50, message = "올바른 사업자 등록번호를 입력해주세요.")
    private String registrationNumber;

    /**
     * 상점 소개글
     * <p>
     * 상점에서 주로 취급하는 품목이나 상점의 특징을 설명하는 소개글입니다.
     */
    @Schema(example = "질 좋은 상품만 판매하는 오픈마켓입니다.")
    private String description;

    /**
     * 정산 지급 은행명
     */
    @Schema(example = "한국은행")
    @NotBlank(message = "정산 받을 은행명은 필수 입력값입니다.")
    @Size(max = 20, message = "은행명은 20자 이하로 입력해주세요.")
    private String bankName;

    /**
     * 정산 계좌번호
     * <p>
     * 판매 대금을 정산받을 계좌의 번호입니다.
     */
    @Schema(example = "110-123-456789")
    @NotBlank(message = "정산 계좌번호는 필수 입력값입니다.")
    @Size(max = 50, message = "계좌번호는 50자 이하로 입력해주세요.")
    private String accountNumber;

    /**
     * 정산 계좌 예금주명
     */
    @Schema(example = "홍길동")
    @NotBlank(message = "예금주명은 필수 입력값입니다.")
    @Size(max = 50, message = "예금주명은 50자 이하로 입력해주세요.")
    private String accountHolder;
}