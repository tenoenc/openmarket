package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ListWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressReadApi {

    private final AddressReadService addressReadService;

    @GetMapping("/addresses")
    @Operation(summary = "내 배송지 목록 조회 API")
    @ApiErrorCodeExamples({GlobalErrorCode.USER_AUTHENTICATION_REQUIRED,})
    public ApiResponse<ListWrapper<AddressInfoResponse>> getMyAddresses(
        @AuthenticationPrincipal Long userId
    ) {
        List<AddressInfoResponse> responses = addressReadService.getAddresses(userId);
        return ApiResponse.success(ListWrapper.of(responses));
    }

    @GetMapping("/addresses/{addressId}")
    @Operation(summary = "내 배송지 상세 조회 API")
    @ApiErrorCodeExamples({
        GlobalErrorCode.USER_AUTHENTICATION_REQUIRED,
        GlobalErrorCode.USER_ADDRESS_NOT_FOUND
    })
    public ApiResponse<AddressInfoResponse> getMyAddressDetail(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long addressId
    ) {
        AddressInfoResponse address = addressReadService.getAddress(addressId, userId);
        return ApiResponse.success(address);
    }
}
