package com.teno.openmarket.common.exception;

import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ResultType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.teno.openmarket.common.response.ApiResponse.fail;
import static com.teno.openmarket.common.response.ApiResponse.ValidationError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid 검증 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<ValidationError> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> ValidationError.of(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        // TODO: 에러 코드 정의 완료 시 하드 코딩 제거
        return fail(ResultType.FAIL, "SYSTEM_INVALID_INPUT", "입력값이 올바르지 않습니다.", errors);
    }
}
