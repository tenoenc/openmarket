package com.teno.openmarket.api.controller;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/response/error")
    @Operation(summary = "에러 응답 자동화 테스트", description = "커스텀 애노테이션을 통해 에러 예시가 자동으로 문서화되는지 확인합니다.")
    @ApiErrorCodeExamples({
        GlobalErrorCode.DEAL_OUT_OF_STOCK,
        GlobalErrorCode.DEAL_NOT_OPEN,
        GlobalErrorCode.USER_NOT_FOUND
    })
    public ApiResponse<Void> errorTest() {
        throw new BusinessException(GlobalErrorCode.DEAL_OUT_OF_STOCK);
    }
}
