package com.teno.openmarket.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class JwtAuthenticationFilterTest {

    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private TokenBlacklistValidator tokenBlacklistValidator;
    @Mock private ObjectMapper objectMapper;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;
    @Mock private PrintWriter writer;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        jwtAuthenticationFilter = new JwtAuthenticationFilter(
                jwtTokenProvider,
                tokenBlacklistValidator,
                objectMapper
        );

        SecurityContextHolder.clearContext();

        given(response.getWriter()).willReturn(writer);
    }

    @Test
    @DisplayName("유효한 토큰이고 블랙리스트에 없다면 SecurityContext에 인증 정보가 등록된다")
    void should_Authenticate_When_ValidTokenExistsAndNotBlacklisted() throws ServletException, IOException {
        // given
        String validToken = "valid.jwt.token";
        given(request.getHeader("Authorization")).willReturn("Bearer " + validToken);
        given(jwtTokenProvider.validateToken(validToken)).willReturn(true);

        given(tokenBlacklistValidator.existsByAccessToken(validToken)).willReturn(false);

        Authentication authentication = mock(Authentication.class);
        given(jwtTokenProvider.getAuthentication(validToken)).willReturn(authentication);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰이 블랙리스트에 등록되어 있다면, 인증하지 않고 401 에러 응답을 보낸다")
    void should_ReturnError_When_TokenIsBlacklisted() throws ServletException, IOException {
        // given
        String blacklistedToken = "logout.jwt.token";
        given(request.getHeader("Authorization")).willReturn("Bearer " + blacklistedToken);
        given(jwtTokenProvider.validateToken(blacklistedToken)).willReturn(true);

        // 블랙리스트에 있다고 설정 -> 예외 발생 유도
        given(tokenBlacklistValidator.existsByAccessToken(blacklistedToken)).willReturn(true);

        given(objectMapper.writeValueAsString(any())).willReturn("{}");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        // 1. 인증 객체가 저장되지 않아야 함 (Context 비어있음)
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // 2. 다음 필터로 진행하지 않아야 함 (중단)
        verify(filterChain, never()).doFilter(request, response);

        // 3. 에러 응답 처리가 되어야 함
        verify(response).setStatus(GlobalErrorCode.SECURITY_LOGOUT.getStatus().value());

        // 4. JSON 응답 작성이 수행되어야 함
        verify(response).getWriter();
        verify(writer).write(anyString());
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
        verify(jwtTokenProvider, never()).validateToken(Mockito.anyString());
        // 4. 블랙리스트 검사도 하지 않아야 함
        verify(tokenBlacklistValidator, never()).existsByAccessToken(anyString());
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
        // 3. 유효하지 않으면 블랙리스트 검사는 스킵해야 함
        verify(tokenBlacklistValidator, never()).existsByAccessToken(anyString());
    }
}
