package com.teno.openmarket.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "공통 응답 포맷")
public class ApiResponse<T> {

    @Schema(description = "결과 상태", allowableValues = {"SUCCESS", "FAIL"}, example = "SUCCESS")
    private final ResultType result;

    @Schema(description = "응답 데이터 (성공 시 데이터, 실패 시 null)")
    private final T data;

    @Schema(description = "에러 메시지 (실패 시에만 포함)", example = "서버 내부 오류가 발생했습니다.")
    private final String message;

    @Schema(description = "에러 코드 (실패 시에만 포함)", example = "SYSTEM_ERROR")
    private final String errorCode;

    @Schema(description = "검증 에러 목록 (유효성 검사 실패 시에만 포함)")
    private final List<ValidationError> errors;

    @Schema(description = "응답을 생성한 모듈명", example = "openmarket-api")
    private final String module;

    @Schema(description = "응답 생성 시각", example = "2026-02-15T15:30:00+09:00")
    private final ZonedDateTime timestamp;

    @Schema(description = "HTTP 요청 고유 ID (X-Request-ID 헤더 값)", example = "8b1a2c3d")
    private final String requestId;

    @Schema(description = "로그 추적용 ID (MDC TraceId)", example = "8b1a2c3d")
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
     * X-Request-ID 주입 (Wither Method)
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

    /**
     * Trace ID 주입 (Wither Method)
     */
    public ApiResponse<T> withTraceId(String traceId) {
        return ApiResponse.<T>builder()
                .result(this.result)
                .data(this.data)
                .message(this.message)
                .errorCode(this.errorCode)
                .errors(this.errors)
                .module(this.module)
                .timestamp(this.timestamp)
                .requestId(this.requestId)
                .traceId(traceId)
                .build();
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ValidationError {
        private final String field;
        private final String reason;
    }
}
