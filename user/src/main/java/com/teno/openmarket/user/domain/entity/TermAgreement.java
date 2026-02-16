package com.teno.openmarket.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * [약관 동의 이력 엔티티]
 * <p>
 * 사용자가 특정 시점에 어떤 약관에 동의했는지에 대한 이력을 관리합니다.
 * 추후 법적 분쟁 발생 시, 사용자가 당시 동의했던 약관의 버전과 내용을 증명하기 위한 감사(Audit) 데이터입니다.
 */
@Entity
@Table(name = "term_agreements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TermAgreement {

    /**
     * 동의 이력 고유 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자
     * <p>
     * 약관에 동의한 사용자 정보입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 약관 ID
     * <p>
     * 사용자가 동의한 약관({@link Term})의 ID입니다.
     * <p>
     * <strong>설계 의도:</strong>
     * {@code Term} 엔티티와 직접적인 연관관계({@code @ManyToOne})를 맺지 않고 ID 값만 보관합니다.
     * 이는 약관 데이터가 변경되거나 물리적으로 삭제되더라도, 동의 이력 자체는 독립적인 사실로 보존하기 위함입니다.
     */
    @Column(name = "term_id", nullable = false)
    private Long termId;

    /**
     * 동의 일시
     * <p>
     * 사용자가 '동의함' 버튼을 클릭하여 서버에 요청이 도달한 시점의 타임스탬프입니다.
     */
    @Column(name = "agreed_at", nullable = false)
    @Builder.Default
    private LocalDateTime agreedAt = LocalDateTime.now();
}