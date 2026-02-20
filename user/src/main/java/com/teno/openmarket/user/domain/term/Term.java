package com.teno.openmarket.user.domain.term;

import com.teno.openmarket.core.security.entity.BaseCreatedTimeEntity;
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
 * [약관 엔티티]
 * <p>
 * 서비스 이용약관, 개인정보 처리방침 등 시스템 내에서 관리되는 약관 정보를 관리합니다.
 * 약관은 내용이 변경될 경우 수정하는 것이 아니라, 새로운 버전의 데이터를 생성하여 관리(Versioning)합니다.
 */
@Entity
@Table(name = "terms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Term extends BaseCreatedTimeEntity {

    /**
     * 약관 고유 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 약관 제목
     * <p>
     * 예: "서비스 이용약관", "개인정보 수집 및 이용 동의" 등
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 약관 본문
     * <p>
     * 약관의 구체적인 내용을 담습니다. HTML 또는 텍스트 형식이 저장될 수 있습니다.
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 약관 버전
     * <p>
     * 약관의 개정 이력을 식별하기 위한 버전 정보입니다. (예: v1.0, v1.1, 2024-02-02 등)
     */
    @Column(name = "version", nullable = false, length = 20)
    private String version;

    /**
     * 필수 동의 여부
     * <p>
     * 회원가입 시 반드시 동의해야 하는 약관인지 여부입니다.
     * <ul>
     * <li>{@code true}: 필수 약관 (미동의 시 가입 불가)</li>
     * <li>{@code false}: 선택 약관 (마케팅 정보 수신 등)</li>
     * </ul>
     */
    @Column(name = "is_required")
    @Builder.Default
    private Boolean isRequired = true;

    /**
     * 활성 상태 여부
     * <p>
     * 현재 사용자에게 노출되어 동의를 받을 수 있는 유효한 약관인지 나타냅니다.
     * 구 버전 약관은 {@code false}로 설정되어 더 이상 노출되지 않습니다.
     */
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}