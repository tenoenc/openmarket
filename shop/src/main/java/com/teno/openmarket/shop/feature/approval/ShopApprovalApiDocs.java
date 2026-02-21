package com.teno.openmarket.shop.feature.approval;

import com.teno.openmarket.common.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.exception.ShopErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin Shop", description = "관리자 상점 관리 API")
@SecurityErrorDocs
public interface ShopApprovalApiDocs {

    @Operation(summary = "상점 입점 승인/반려")
    @GlobalErrorCodeExamples({GlobalErrorCode.SYSTEM_INVALID_INPUT})
    @ShopErrorCodeExamples({ShopErrorCode.SHOP_NOT_FOUND, ShopErrorCode.SHOP_NOT_WAITING_STATUS})
    ApiResponse<Void> approveShop(Long shopId, ShopApprovalRequest request);
}
