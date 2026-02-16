package com.teno.openmarket.api.controller;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.security.Principal;
import java.util.List;

@RestController
public class TestApi {

    public enum TestCategory {
        ELECTRONICS, FASHION, FOOD
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "테스트 응답 DTO")
    public static class TestDto {
        @Schema(description = "아이디", example = "1")
        private Long id;
        private String name;
    }

    @GetMapping("/response/error")
    @Operation(summary = "에러 응답 자동화 테스트",
            description = "커스텀 애노테이션을 통해 에러 예시가 자동으로 문서화되는지 확인합니다.")
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

    @GetMapping("/exclusion-test")
    @Operation(summary = "파라미터 제외 테스트",
            description = "HttpServletRequest, HttpSession, Principal 등이 문서에서 숨겨지는지 확인합니다.")
    public ApiResponse<String> exclusionTest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Principal principal,
            @RequestParam(name = "search") String search
    ) {
        return ApiResponse.success("검증용 API: " + search);
    }

    @GetMapping("/wrapper-test")
    @Operation(summary = "공통 응답 래퍼 테스트",
            description = "ApiResponse<T>가 실제 데이터 구조를 어떻게 감싸서 보여주는지 확인합니다.")
    public ApiResponse<ListWrapper<TestDto>> wrapperTest() {
        List<TestDto> list = List.of(
            new TestDto(1L, "Teno1"),
            new TestDto(2L, "Teno2")
        );
        return ApiResponse.success(list);
    }
}
