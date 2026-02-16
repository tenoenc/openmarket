package com.teno.openmarket.user.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JwtAuthenticationFilterTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("유효한 토큰이 Authorization 헤더에 있으면 SecurityContext에 인증 정보가 등록된다")
    void should_Authenticate_When_ValidTokenExists() throws ServletException, IOException {
        // given
        String validToken = "valid.jwt.token";
        given(request.getHeader("Authorization")).willReturn("Bearer " + validToken);
        given(jwtTokenProvider.validateToken(validToken)).willReturn(true);

        Authentication authentication = mock(Authentication.class);
        given(jwtTokenProvider.getAuthentication(validToken)).willReturn(authentication);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("헤더에 토큰이 없으면 인증 과정 없이 다음 필터로 진행한다")
    void should_PassThrough_When_TokenIsMissing() throws ServletException, IOException {
        // given
        given(request.getHeader("Authorization")).willReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        // 1. 인증 객체가 없어야 함
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // 2. 다음 필터로 넘어가야 함
        verify(filterChain).doFilter(request, response);
        // 3. 토큰 검증 로직은 호출되지 않아야 함
        verify(jwtTokenProvider, Mockito.never()).validateToken(Mockito.anyString());
    }

    @Test
    @DisplayName("토큰이 유효하지 않으면 인증 객체를 저장하지 않고 다음 필터로 진행된다")
    void should_NotAuthenticate_When_TokenIsInvalid() throws ServletException, IOException {
        // given
        String invalidToken = "invalid.token.value";
        given(request.getHeader("Authorization")).willReturn("Bearer " + invalidToken);
        given(jwtTokenProvider.validateToken(invalidToken)).willReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        // 1. 인증 객체가 없어야 함
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // 2. 다음 필터로 넘어가야 함
        verify(filterChain).doFilter(request, response);
    }
}
