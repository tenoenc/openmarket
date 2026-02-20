package com.teno.openmarket.user.feature.address.update;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface AddressUpdateApiDocs {

    @Operation(summary = "배송지 수정 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @UserErrorCodeExamples(UserErrorCode.USER_ADDRESS_NOT_FOUND)
    ApiResponse<Void> updateAddress(Long userId, Long addressId, AddressUpdateRequest request);
}
