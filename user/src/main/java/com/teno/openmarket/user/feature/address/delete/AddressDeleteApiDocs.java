package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface AddressDeleteApiDocs {

    @Operation(summary = "배송지 삭제 API")
    @UserErrorCodeExamples({
        UserErrorCode.USER_ADDRESS_NOT_FOUND,
        UserErrorCode.USER_ADDRESS_DEFAULT
    })
    ApiResponse<Void> deletedAddress(Long userId, Long addressId);
}
