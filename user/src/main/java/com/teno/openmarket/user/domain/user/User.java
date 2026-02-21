package com.teno.openmarket.user.domain.user;

import com.teno.openmarket.common.entity.BaseTimeEntity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
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
    @Builder.Default
    private Role role = Role.ROLE_USER;

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

    public void updateProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * 판매자 권한으로 승격합니다.
     * <p>
     * 이미 판매자이거나 상위 권한(관리자)인 경우 상태를 변경하지 않습니다.
     */
    public void upgradeToSeller() {
        // 관리자는 이미 모든 권한을 대리할 수 있으므로 강등시키지 않음
        if (this.role == Role.ROLE_ADMIN) {
            return;
        }
        // 이미 판매자 권한인 경우 멱등성 보장을 위해 무시
        if (this.role == Role.ROLE_SELLER) {
            return;
        }

        this.role = Role.ROLE_SELLER;
    }
}
