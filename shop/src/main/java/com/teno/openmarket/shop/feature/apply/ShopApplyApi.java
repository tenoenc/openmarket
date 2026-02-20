package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopApplyApi implements ShopApplyApiDocs {

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
    @Override
    @PostMapping("/apply")
    public ApiResponse<Void> applyForShop(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ShopApplyRequest request
    ) {
        ShopApplyCommand command = shopApplicationMapper.toCommand(request);
        shopApplicationService.apply(userId, command);
        return ApiResponse.success();
    }
}
