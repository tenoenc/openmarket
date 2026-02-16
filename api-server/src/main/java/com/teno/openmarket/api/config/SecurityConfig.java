package com.teno.openmarket.api.config;

import com.teno.openmarket.user.infra.security.entrypoint.JwtAuthenticationEntryPoint;
import com.teno.openmarket.user.infra.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * [애플리케이션 전역 보안 및 실행 설정]
 * <p>
 * 실행 모듈 레벨에서 시스템 전반에 적용되는 기본 보안 정책을 정의합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * HTTP 보안 필터 체인 설정
     *
     * <ul>
     * <li>{@code @Order(2)}: 도메인별 특화 설정({@code @Order(1)})이 매칭되지 않은 나머지 요청들을 최종 처리합니다.</li>
     * </ul>
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     */
    @Bean
    @Order(2)
    public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (REST API이므로 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            // 2. Form Login 비활성화 (JSON 방식 사용)
            .formLogin(AbstractHttpConfigurer::disable)
            // 3. Basic Auth 비활성화
            .httpBasic(AbstractHttpConfigurer::disable)
            // 4. 세션 설정: Stateless (JSESSIONID 사용 안 함)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        // 5. URL별 권한 설정
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
        );

        // 6. 익명 사용자 인증 처리 방식 대응 (403 -> 401)
        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );

        // 7. JWT 필터 등록
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
