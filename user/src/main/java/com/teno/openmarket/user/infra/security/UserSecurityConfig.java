package com.teno.openmarket.user.infra.security;

import com.teno.openmarket.common.security.JwtAuthenticationEntryPoint;
import com.teno.openmarket.common.security.JwtAuthenticationFilter;
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
 * [사용자 도메인 특화 보안 설정]
 * <p>
 * User 도메인에 속한 엔드포인트의 보안 정책을 정의합니다.
 * JWT 기반 인증을 사용하므로 세션 생성 및 불필요한 기본 인증 방식을 비활성화합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class UserSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 비밀번호 암호화를 위한 Encoder 빈 등록
     * <p>
     * BCrypt 해싱 함수를 사용하며, 기본 강도(Strength 10)를 적용합니다.
     * DB에 저장되는 비밀번호는 이 Encoder를 통해 암호화되어야 합니다.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 보안 필터 체인 설정
     *
     * <ul>
     * <li>{@code @Order(1)}: 전역 설정보다 우선적으로 평가되어 도메인 요청을 가로채 처리합니다.</li>
     * </ul>
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     */
    @Bean
    @Order(1)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 아래 URL 패턴에만 필터 체인 적용
            .securityMatcher("/api/v1/auth/**", "/api/v1/users/**")

            // 2. CSRF 비활성화 (REST API이므로 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            // 3. Form Login 비활성화 (JSON 방식 사용)
            .formLogin(AbstractHttpConfigurer::disable)
            // 4. Basic Auth 비활성화
            .httpBasic(AbstractHttpConfigurer::disable)
            // 5. 세션 설정: Stateless (JSESSIONID 사용 안 함)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        // 6. URL별 권한 설정
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/v1/auth/signup",
                "/api/v1/auth/login",
                "/api/v1/auth/reissue"
            ).permitAll()
            .anyRequest().authenticated()
        );

        // 7. 익명 사용자 인증 처리 방식 대응 (403 -> 401)
        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401 처리
        );

        // 8. JWT 필터 등록
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
