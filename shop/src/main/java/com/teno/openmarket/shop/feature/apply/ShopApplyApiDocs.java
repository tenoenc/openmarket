package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.exception.ShopErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Shop", description = "상점 관리 API")
@SecurityErrorDocs
public interface ShopApplyApiDocs {

    @Operation(summary = "상점 입점 신청 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @ShopErrorCodeExamples({
        ShopErrorCode.SHOP_ALREADY_EXISTS,
        ShopErrorCode.SHOP_NAME_DUPLICATED
    })
    ApiResponse<Void> applyForShop(Long userId, ShopApplyRequest request);
}
