package com.teno.openmarket.user.domain.entity;

import com.teno.openmarket.common.entity.BaseTimeEntity;
import com.teno.openmarket.user.domain.vo.Role;
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

import java.time.LocalDateTime;

/**
 * [사용자 엔티티]
 * <p>
 * 시스템의 핵심 사용자 정보를 관리하는 엔티티입니다.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    /**
     * 사용자 고유 식별자 (PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이메일
     * <p>
     * 로그인 아이디로 사용되는 이메일입니다.
     * 시스템 내에서 고유해야 하며, 가입 시 중복 체크가 필수입니다.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 비밀번호
     * <p>
     * 보안을 위해 Bcrypt 단방향 암호화 알고리즘이 적용된 비밀번호입니다.
     * 평문 저장은 엄격히 금지됩니다.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 실명
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 연락처
     */
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    /**
     * 사용자 권한 등급
     * <p>
     * 사용자의 권한 등급 (USER, SELLER, ADMIN)입니다.
     * {@code ROLE_SELLER} 권한은 관리자의 입점 승인 프로세스 완료 시점에 부여됩니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    /**
     * 탈퇴 여부
     * <p>
     * 서비스 탈퇴 여부를 나타내는 플래그입니다.
     * 운영 중인 상점이 있거나 처리 중인 주문이 있는 경우 탈퇴가 제한될 수 있습니다.
     */
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 0")
    @Builder.Default
    private Boolean isDeleted = false;

    /**
     * 탈퇴 일시
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
