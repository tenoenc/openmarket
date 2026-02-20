package com.teno.openmarket.core.security.response;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice(basePackages = "com.teno")
@RequiredArgsConstructor
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 모든 응답 가로채기
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String traceId = MDC.get("traceId");

        ApiResponse<?> apiResponse;


        if (body instanceof ApiResponse<?>) {
            // ApiResponse 타입인 경우 모듈명만 주입
            apiResponse = (ApiResponse<?>) body;
        } else if (body instanceof List<?>) {
            // List 타입인 경우 ListWrapper로 감싸고 + ApiResponse로 감싸고 + 모듈명 주입
            apiResponse = ApiResponse.success(ListWrapper.of((List<?>) body));
        } else {
            // 그 외인 경우 ApiResponse로 감싸고 + 모듈명 주입
            // (단, String을 직접 리턴하는 경우 StringHttpMessageConverter와 충돌 날 수 있음)
            apiResponse = ApiResponse.success(body);
        }

        apiResponse = apiResponse.withModule(applicationName);

        if (traceId != null) {
            apiResponse = apiResponse.withRequestId(traceId)
                    .withTraceId(traceId);
        }

        return apiResponse;
    }
}
