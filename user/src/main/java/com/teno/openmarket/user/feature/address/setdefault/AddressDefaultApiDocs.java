package com.teno.openmarket.user.feature.address.setdefault;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface AddressDefaultApiDocs {

    @Operation(summary = "기본 배송지 설정 API")
    @UserErrorCodeExamples(UserErrorCode.USER_ADDRESS_NOT_FOUND)
    ApiResponse<Void> setDefaultAddress(Long userId, Long addressId);
}
