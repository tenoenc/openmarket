package com.teno.openmarket.user.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teno.openmarket.common.error.ErrorCode;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ResultType;
import com.teno.openmarket.user.domain.token.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Request Header에서 토큰 추출
            String token = resolveToken(request);

            // 2. 토큰 유효성 검사 (유효하면 인증 정보 저장)
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // 2-1. 블랙리스트(로그아웃) 여부 확인
                if (tokenBlacklistRepository.existsByAccessToken(token)) {
                    // 이미 로그아웃된 토큰이므로 인증 거부
                    throw new BusinessException(GlobalErrorCode.USER_LOGOUT);
                }

                // 2-2. 정상 토큰이면 인증 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 3. 다음 필터로 진행
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            // BusinessException은 우리가 의도한 예외이므로 JSON 응답 반환
            setErrorResponse(response, e.getErrorCode());
        } catch (Exception e) {
            // 기타 예상치 못한 예외 처리
            log.error("Unhandled Exception: ", e);
            setErrorResponse(response, GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiResponse<Void> apiResponse = ApiResponse.fail(
                ResultType.FAIL,
                errorCode.name(),
                errorCode.getMessage()
        );

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}


