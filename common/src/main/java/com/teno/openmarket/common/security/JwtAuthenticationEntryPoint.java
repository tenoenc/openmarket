package com.teno.openmarket.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teno.openmarket.common.error.ErrorCode;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ResultType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할 때 401 Unauthorized 에러를 리턴
        setErrorResponse(response, GlobalErrorCode.SECURITY_AUTHENTICATION_REQUIRED);
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

        apiResponse = apiResponse.withModule(applicationName);

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}