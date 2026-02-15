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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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
                        "/api/v1/auth/reissue"
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
