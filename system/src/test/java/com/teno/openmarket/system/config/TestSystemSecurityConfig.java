package com.teno.openmarket.system.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [시스템 도메인 테스트 전용 보안 설정]
 * <p>
 * System 모듈 내의 기능 테스트 시, 실제 운영 환경의 복잡한 보안 필터 체인에 의존하지 않고
 * 독립적인 테스트 환경을 구축하기 위해 사용되는 테스트 설정 클래스입니다.
 */
@TestConfiguration
public class TestSystemSecurityConfig {

    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/api/v1/system/**",
                "/api/v1/admin/**",
                "/api/v1/internal/**",
                "/api/v1/settlements/**"
            )

            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/api/v1/system/health",
                        "/api/v1/system/server-time"
                    ).permitAll()
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}