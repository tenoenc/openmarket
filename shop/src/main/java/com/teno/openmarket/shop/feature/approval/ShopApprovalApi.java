package com.teno.openmarket.shop.feature.approval;

import com.teno.openmarket.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/shops")
@RequiredArgsConstructor
public class ShopApprovalApi implements ShopApprovalApiDocs {

    private final ShopApprovalService shopApprovalService;

    /**
     * 상점 입점 승인 및 반려
     * <p>
     * 관리자가 대기 중인 상점의 입점 신청을 검토하여 승인하거나 반려합니다.
     *
     * @param shopId  승인 또는 반려할 상점의 고유 식별자
     * @param request 승인 여부와 반려 사유를 담은 요청 객체
     * @return {@link ApiResponse} 성공 응답
     */
    @Override
    @PatchMapping("/{shopId}/approval")
    public ApiResponse<Void> approveShop(
        @PathVariable Long shopId,
        @RequestBody @Valid ShopApprovalRequest request
    ) {
        ShopApprovalCommand command = ShopApprovalCommand.builder()
                .decision(request.getDecision())
                .rejectReason(request.getRejectReason())
                .build();

        shopApprovalService.processApproval(shopId, command);

        return ApiResponse.success();
    }
}
