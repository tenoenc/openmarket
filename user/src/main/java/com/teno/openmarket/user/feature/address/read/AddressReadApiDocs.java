package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.core.security.response.ListWrapper;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface AddressReadApiDocs {

    @Operation(summary = "내 배송지 목록 조회 API")
    ApiResponse<ListWrapper<AddressInfoResponse>> getMyAddresses(Long userId);

    @Operation(summary = "내 배송지 상세 조회 API")
    @UserErrorCodeExamples(UserErrorCode.USER_ADDRESS_NOT_FOUND)
    ApiResponse<AddressInfoResponse> getMyAddressDetail(Long userId, Long addressId);
}
