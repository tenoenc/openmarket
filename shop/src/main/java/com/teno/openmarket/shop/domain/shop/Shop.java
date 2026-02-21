package com.teno.openmarket.shop.domain.shop;

import com.teno.openmarket.common.entity.BaseTimeEntity;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * [상점(Shop) 엔티티]
 * <p>
 * 판매자가 운영하는 상점의 도메인 모델입니다.
 * 1인 1상점 정책에 따라 userId는 고유(Unique) 제약조건을 가지며,
 * 관리자의 승인 절차를 통해 상태(WAITING, ACTIVE, REJECTED)가 변경됩니다.
 */
@Entity
@Table(name = "shops")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Shop extends BaseTimeEntity {

    /**
     * 상점 고유 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 상점을 소유한 회원의 고유 식별자
     * <p>
     * 1인 1상점 원칙으로 인해 회원당 하나의 상점만 보유할 수 있습니다.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * 상점명
     * <p>
     * 플랫폼 내에서 중복될 수 없는 고유한 상점의 이름입니다.
     */
    @Column(name = "shop_name", nullable = false, length = 100, unique = true)
    private String shopName;

    /**
     * 사업자 등록번호
     * <p>
     * 상점 개설을 위한 필수 법적 식별 정보입니다.
     */
    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    /**
     * 상점 소개
     * <p>
     * 상점에 대한 상세한 설명이나 소개를 담는 긴 텍스트 필드입니다.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 정산 지급 은행명
     */
    @Column(name = "bank_name", length = 20)
    private String bankName;

    /**
     * 정산 계좌번호
     * <p>
     * 판매 대금을 정산받을 계좌의 번호입니다.
     */
    @Column(name = "account_number", length = 50)
    private String accountNumber;

    /**
     * 정산 계좌 예금주명
     */
    @Column(name = "account_holder", length = 50)
    private String accountHolder;

    /**
     * 상점 승인 상태
     * <p>
     * WAITING(대기), ACTIVE(활성), REJECTED(거절) 상태를 가집니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ShopStatus status;

    /**
     * 입점 반려 사유
     * <p>
     * 관리자가 입점 신청을 거절(REJECTED)할 경우, 해당 사유를 기록하는 필드입니다.
     */
    @Column(name = "reject_reason")
    private String rejectReason;

    @Builder
    public Shop(Long userId, String shopName, String registrationNumber, String description,
                String bankName, String accountNumber, String accountHolder, ShopStatus status) {
        this.userId = userId;
        this.shopName = shopName;
        this.registrationNumber = registrationNumber;
        this.description = description;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.status = status != null ? status : ShopStatus.WAITING;
    }

    /**
     * 상점 입점 승인
     */
    public void approve() {
        if (this.status != ShopStatus.WAITING) {
            throw new BusinessException(ShopErrorCode.SHOP_NOT_WAITING_STATUS);
        }
        this.status = ShopStatus.ACTIVE;
    }

    /**
     * 상점 입점 반려
     */
    public void reject(String rejectReason) {
        if (this.status != ShopStatus.WAITING) {
            throw new BusinessException(ShopErrorCode.SHOP_NOT_WAITING_STATUS);
        }
        this.status = ShopStatus.REJECTED;
        this.rejectReason = rejectReason;
    }
}
