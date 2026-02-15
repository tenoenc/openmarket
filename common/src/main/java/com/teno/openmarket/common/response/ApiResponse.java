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

/**
 * [공통 응답 플랫폼]
 * <p>
 * 모든 API 요청에 대한 표준 응답 래퍼(Wrapper)입니다.
 * 성공/실패 여부와 관계없이 항상 이 구조로 반환됩니다.
 *
 * @param <T> 실제 응답 데이터의 타입 (성공 시에만 포함됨)
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "공통 응답 포맷")
public class ApiResponse<T> {

    /**
     * 결과 상태
     * <ul>
     * <li>SUCCESS: 요청 처리 성공</li>
     * <li>FAIL: 요청 처리 실패 (비즈니스 로직 에러 포함)</li>
     * </ul>
     */
    @Schema(example = "SUCCESS")
    private final ResultType result;

    /**
     * 응답 데이터 Payload
     * <p>
     * 요청이 성공적으로 처리되었을 때 반환되는 실제 데이터입니다.
     * 실패 시에는 null이 반환됩니다.
     */
    @Schema
    private final T data;

    /**
     * 에러 메시지
     * <p>
     * 사용자에게 노출 가능한 수준의 에러 설명입니다.
     * 성공 시에는 null 또는 빈 문자열입니다.
     */
    @Schema(example = "서버 내부 오류가 발생했습니다.")
    private final String message;

    /**
     * 에러 코드 (Business Error Code)
     * <p>
     * 클라이언트가 에러 유형을 식별하고 분기 처리를 하기 위한 고유 코드입니다.
     * (예: DEAL_OUT_OF_STOCK, USER_NOT_FOUND)
     */
    @Schema(example = "SYSTEM_ERROR")
    private final String errorCode;

    /**
     * 필드 유효성 검증 실패 목록
     * <p>
     * 검증 실패 시, 어떤 필드가 왜 실패했는지 상세 내역을 제공합니다.
     */
    @Schema
    private final List<ValidationError> errors;

    /**
     * 응답 생성 모듈
     * <p>
     * 어떤 서버/모듈에서 이 응답을 생성했는지 식별합니다.
     */
    @Schema(example = "openmarket-api")
    private final String module;

    /**
     * 응답 생성 타임스탬프 (ISO-8601)
     */
    @Schema(example = "2026-02-15T15:30:00+09:00")
    private final ZonedDateTime timestamp;

    /**
     * 요청 추적 ID (Request ID)
     * <p>
     * 클라이언트가 보낸 X-Request-ID 헤더 값 혹은 서버에서 생성한 난수입니다.
     * 로그 추적 시 이 값을 사용합니다.
     */
    @Schema(example = "8b1a2c3d")
    private final String requestId;

    /**
     * 분산 추적 ID (Trace ID)
     * <p>
     * MSA 환경에서 요청의 전체 흐름을 추적하기 위한 유니크 ID입니다.
     */
    @Schema(example = "8b1a2c3d")
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

    /**
     * 필드별 검증 에러 상세
     */
    @Getter
    @AllArgsConstructor(staticName = "of")
    public static class ValidationError {
        private final String field;
        private final String reason;
    }
}
