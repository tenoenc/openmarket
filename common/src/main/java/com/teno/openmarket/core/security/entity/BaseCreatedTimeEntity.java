package com.teno.openmarket.core.security.entity;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * [생성 시간 감사 엔티티]
 * <p>
 * 데이터가 생성된 시간({@code created_at})만 관리하는 엔티티입니다.
 * </p>
 * <strong>사용 대상</strong>
 * <ul>
 * <li>약관 (Term) - 버전 관리로 인해 불변</li>
 * <li>약관 동의 이력 (TermAgreement) - 이력 데이터</li>
 * <li>재고/결제 이력 (History) - 로그성 데이터</li>
 * <li>아웃박스 이벤트 (OutboxEvent) - 발행 기록</li>
 * </ul>
 */
public class BaseCreatedTimeEntity {

    /**
     * 생성일자
     * <p>
     * 엔티티가 처음 생성되어 저장될 때의 일시입니다.
     * <ul>
     * <li>{@link CreatedDate}: {@code persist} 이벤트 발생 시 현재 시각을 자동으로 할당합니다.</li>
     * </ul>
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
