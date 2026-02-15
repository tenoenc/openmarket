package com.teno.openmarket.api.controller;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController
public class TestController {

    public enum TestCategory {
        ELECTRONICS, FASHION, FOOD
    }

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

    @GetMapping("/ui-test")
    @Operation(summary = "UI 컴포넌트 테스트",
            description = "1. category가 드롭다운(Select)으로 나오는지 확인\n"
                    + "2. pageable이 page, size, sort 3개만 깔끔하게 나오는지 확인")
    public ApiResponse<String> uiTest(
            @RequestParam(name = "category") TestCategory category,
            Pageable pageable
    ) {
        return ApiResponse.success("테스트 통과: " + category + ", " + pageable);
    }
}
