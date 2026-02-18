package com.teno.openmarket.system.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [시스템 도메인 테스트 전용 보안 설정]
 * <p>
 * System 모듈 내의 기능 테스트 시, 실제 운영 환경의 복잡한 보안 필터 체인에 의존하지 않고
 * 독립적인 테스트 환경을 구축하기 위해 사용되는 테스트 설정 클래스입니다.
 */
@TestConfiguration
public class TestSystemSecurityConfig {

    // 테스트 편의를 위해 모든 요청을 허용하는 필터 체인
    @Bean
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}