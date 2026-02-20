package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCodeExamples;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.exception.ShopErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shop", description = "상점 관리 API")
@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopApplyApi {

    private final ShopApplyService shopApplicationService;
    private final ShopApplyMapper shopApplicationMapper;

    /**
     * 상점 입점 신청 API
     * <p>
     * 상점 개설을 신청합니다. 신청 즉시 판매가 가능한 것은 아니며, 관리자의 승인 대기(WAITING) 상태로 등록됩니다.
     * 1인당 1개의 상점만 개설할 수 있습니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @param request 입점 신청할 상점의 상세 정보 (상점명, 소개 등)
     * @return {@link ApiResponse} 성공 응답
     */
    @PostMapping("/apply")
    @Operation(summary = "상점 입점 신청 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @SecurityErrorCodeExamples(SecurityErrorCode.SECURITY_AUTHENTICATION_REQUIRED)
    @ShopErrorCodeExamples({
        ShopErrorCode.SHOP_ALREADY_EXISTS,
        ShopErrorCode.SHOP_NAME_DUPLICATED
    })
    public ApiResponse<Void> applyForShop(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ShopApplyRequest request
    ) {
        ShopApplyCommand command = shopApplicationMapper.toCommand(request);
        shopApplicationService.apply(userId, command);
        return ApiResponse.success();
    }
}
