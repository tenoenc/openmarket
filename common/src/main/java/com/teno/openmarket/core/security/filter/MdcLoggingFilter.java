package com.teno.openmarket.core.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * 요청 추적을 위한 MDC 설정 필터
 * - 모든 요청에 TraceId를 부여하고 로그 컨텍스트 저장
 * - 응답 헤더에 X-Request-ID 자동 추가
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String X_REQUEST_ID = "X-Request-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. TraceId 생성
        String traceId = httpRequest.getHeader(X_REQUEST_ID);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().substring(0, 8);
        }

        try {
            // 2. MDC에 TraceId 저장
            MDC.put(TRACE_ID, traceId);

            // 3. 응답 헤더에 X-Request-ID 추가
            httpResponse.setHeader(X_REQUEST_ID, traceId);

            chain.doFilter(request, response);
        } finally {
            // 요청 처리 완료 후 MDC 정리 (Thread Pool 사용 시 필수)
            MDC.clear();
        }
    }
}
