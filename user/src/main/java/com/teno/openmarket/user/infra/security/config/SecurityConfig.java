package com.teno.openmarket.user.infra.security.config;

import com.teno.openmarket.user.infra.security.entrypoint.JwtAuthenticationEntryPoint;
import com.teno.openmarket.user.infra.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 핵심 설정 클래스
 * <p>
 * 애플리케이션의 전반적인 보안 정책을 정의합니다.
 * JWT 기반 인증을 사용하므로 세션 생성 및 불필요한 기본 인증 방식을 비활성화합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                "/api/v1/auth/signup",
                "/api/v1/auth/login",
                "/api/v1/auth/reissue",
                "/swagger-ui/**",
                "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
        );

        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401 처리
        );

        // 6. JWT 필터 등록
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
