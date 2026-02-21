package com.teno.openmarket.shop.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [상점 도메인 테스트 전용 보안 설정]
 * <p>
 * Shop 모듈 내의 기능 테스트 시, 실제 운영 환경의 복잡한 보안 필터 체인에 의존하지 않고
 * 독립적인 테스트 환경을 구축하기 위해 사용되는 테스트 설정 클래스입니다.
 */
@TestConfiguration
public class TestShopSecurityConfig {

    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(
                "/api/v1/shops/**",
                "/api/v1/products/**",
                "/api/v1/admin/shops/**"
            )

            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}