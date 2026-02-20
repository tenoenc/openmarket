package com.teno.openmarket.user.domain.address;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [사용자 배송지 엔티티]
 * <p>
 * 사용자가 등록한 배송지 정보를 관리하는 도메인 모델입니다.
 * 최대 등록 개수 제한(5개) 및 기본 배송지 설정 등의 비즈니스 규칙이 적용됩니다.
 */
@Entity
@Table(name = "user_addresses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserAddress {

    /**
     * 배송지 고유 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 배송지를 소유한 사용자의 고유 식별자
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 배송지 별칭 (예: 집, 회사)
     * <p>
     * 선택 입력값이며 최대 20자까지 지정할 수 있습니다.
     */
    @Column(name = "address_name", length = 20)
    private String addressName;

    /**
     * 수령인 이름
     */
    @Column(name = "recipient_name", nullable = false, length = 50)
    private String recipientName;

    /**
     * 수령인 휴대전화 번호
     */
    @Column(name = "recipient_phone", nullable = false, length = 20)
    private String recipientPhone;

    /**
     * 우편번호 (5자리 숫자)
     */
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    /**
     * 기본 주소 (시/군/구 및 도로명)
     */
    @Column(name = "address_base", nullable = false)
    private String addressBase;

    /**
     * 상세 주소 (동/호수 등)
     */
    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    /**
     * 기본 배송지 여부
     * <p>
     * 사용자의 여러 배송지 중 가장 우선적으로 노출되는 기본 배송지인지 나타냅니다.
     * 데이터베이스 생성 시 기본값은 0(false)으로 설정됩니다.
     */
    @Column(name = "is_default", columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private Boolean isDefault = false;

    /**
     * 배송지 정보 수정
     */
    public void updateAddress(String addressName, String recipientName, String recipientPhone,
                              String zipCode, String addressBase, String addressDetail) {
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.zipCode = zipCode;
        this.addressBase = addressBase;
        this.addressDetail = addressDetail;
    }
}
