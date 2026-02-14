package com.teno.openmarket.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.MDC;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final ResultType result;
    private final T data;
    private final String message;
    private final String errorCode;
    private final List<ValidationError> errors;
    private final String module;
    private final ZonedDateTime timestamp;
    private final String requestId;
    private final String traceId;

    /**
     * 성공 응답 (데이터 없음)
     */
    public static ApiResponse<Void> success() {
        return createSuccess(null);
    }

    /**
     * 성공 응답 (단건 데이터)
     */
    public static <T> ApiResponse<T> success(T data) {
        return createSuccess(data);
    }

    /**
     * 성공 응답 (리스트 데이터)
     */
    public static <T> ApiResponse<ListWrapper<T>> success(List<T> data) {
        return createSuccess(ListWrapper.of(data));
    }

    /**
     * 성공 응답 (단건 데이터)
     */
    public static <T> ApiResponse<T> createSuccess(T data) {
        return ApiResponse.<T>builder()
                .result(ResultType.SUCCESS)
                .data(data)
                .errors(Collections.emptyList())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .requestId(UUID.randomUUID().toString())
                .traceId(MDC.get("traceId"))
                .build();
    }

    /**
     * 실패 응답 (메시지 포함)
     */
    public static ApiResponse<Void> fail(ResultType resultType, String errorCode, String message) {
        return ApiResponse.<Void>builder()
                .result(resultType)
                .errorCode(errorCode)
                .message(message)
                .errors(Collections.emptyList())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .requestId(UUID.randomUUID().toString())
                .traceId(MDC.get("traceId"))
                .build();
    }

    /**
     * 실패 응답 (Validation 에러 상세 포함)
     */
    public static ApiResponse<Void> fail(ResultType resultType, String errorCode, String message,
                                         List<ValidationError> errors) {
        return ApiResponse.<Void>builder()
                .result(resultType)
                .errorCode(errorCode)
                .message(message)
                .errors(errors != null ? errors : Collections.emptyList())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Seoul")))
                .requestId(UUID.randomUUID().toString())
                .traceId(MDC.get("traceId"))
                .build();
    }

    /**
     * 모듈명 주입 (Wither Method)
     */
    public ApiResponse<T> withModule(String moduleName) {
        return ApiResponse.<T>builder()
                .result(this.result)
                .data(this.data)
                .message(this.message)
                .errorCode(this.errorCode)
                .errors(this.errors)
                .module(moduleName)
                .timestamp(this.timestamp)
                .requestId(this.requestId)
                .traceId(this.traceId)
                .build();
    }

    /**
     * 헤더의 X-Request-ID 주입 (Wither Method)
     */
    public ApiResponse<T> withRequestId(String requestId) {
        return ApiResponse.<T>builder()
                .result(this.result)
                .data(this.data)
                .message(this.message)
                .errorCode(this.errorCode)
                .errors(this.errors)
                .module(this.module)
                .timestamp(this.timestamp)
                .requestId(requestId)
                .traceId(this.traceId)
                .build();
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ValidationError {
        private final String field;
        private final String reason;
    }
}
