package com.teno.openmarket.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * [공통 시간 감사 엔티티]
 * <p>
 * 모든 엔티티의 공통 속성인 생성 및 수정 시간을 관리하는 추상 클래스입니다.
 *
 * <ul>
 * <li>{@link MappedSuperclass}: 상속받는 자식 엔티티에게 매핑 정보만 제공합니다.</li>
 * <li>{@link EntityListeners}: {@link AuditingEntityListener}를 통해 엔티티의 상태 변화를 감지합니다.</li>
 * </ul>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity extends BaseCreatedTimeEntity {

    /**
     * 수정일자
     * <p>
     * 엔티티의 데이터가 변경될 때의 마지막 일시입니다.
     * <ul>
     * <li>{@link LastModifiedDate}: {@code update} 쿼리가 발생할 때마다 변경된 시각으로 갱신합니다.</li>
     * </ul>
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
