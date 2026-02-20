package com.teno.openmarket.core.security.exception;

import com.teno.openmarket.core.security.error.ErrorCode;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.core.security.response.ResultType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static com.teno.openmarket.core.security.response.ApiResponse.fail;
import static com.teno.openmarket.core.security.response.ApiResponse.ValidationError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 개발자가 의도적으로 던진 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("Business Exception: [{} - {}] {}", errorCode.name(), errorCode.getStatus(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(
                        ResultType.FAIL,
                        errorCode.name(),
                        errorCode.getMessage()
                ));
    }

    /**
     * @Valid 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        GlobalErrorCode errorCode = GlobalErrorCode.SYSTEM_INVALID_INPUT;

        List<ValidationError> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> ValidationError.of(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        log.warn("Validation Error: {} - Errors: {}", errorCode.name(), errors);

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(
                        ResultType.FAIL,
                        errorCode.name(),
                        errorCode.getMessage(),
                        errors
                ));
    }

    /**
     * 잘못된 URL 호출 시 처리
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("No Resource Found: {}", e.getMessage());
        return ResponseEntity
                .status(404)
                .body(ApiResponse.fail(ResultType.FAIL, "SYSTEM_NOT_FOUND",
                        "요청한 리소스를 찾을 수 없습니다."));
    }

    /**
     * 예상치 못한 서버 에러 처리 (최후의 방어선)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unhandled Exception: ", e);

        GlobalErrorCode errorCode = GlobalErrorCode.SYSTEM_ERROR;

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(
                        ResultType.FAIL,
                        errorCode.name(),
                        errorCode.getMessage()
                ));
    }

}
