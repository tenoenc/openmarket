package com.teno.openmarket.user.feature.address.create;

import com.teno.openmarket.common.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface AddressCreateApiDocs {

    @Operation(summary = "배송지 등록 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @UserErrorCodeExamples(UserErrorCode.USER_ADDRESS_LIMIT)
    ApiResponse<Void> createAddress(Long userId, AddressCreateRequest request);
}
