package com.teno.openmarket.common.config;

import jakarta.persistence.EntityListeners;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * [JPA 감사 설정]
 * <p>
 * 애플리케이션 전역에서 엔티티의 생성/수정 정보를 자동으로 기록할 수 있도록 인프라를 구축합니다.
 * <ul>
 * <li>{@link EnableJpaAuditing}: JPA의 {@link EntityListeners}를 활성화하여 감사(Auditing) 정보를 주입합니다.</li>
 * </ul>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}