package com.teno.openmarket.user.feature.address.setdefault;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressDefaultApi {

    private final AddressDefaultService addressDefaultService;

    /**
     * 기본 배송지 설정 API
     * <p>
     * 특정 배송지를 사용자의 기본 배송지로 변경합니다. 기존에 설정된 기본 배송지는 자동으로 일반 배송지로 변경됩니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @param addressId 기본 배송지로 설정할 배송지의 식별자 (경로 변수)
     * @return {@link ApiResponse} 성공 응답
     */
    @PatchMapping("/addresses/{addressId}/default")
    @Operation(summary = "기본 배송지 설정 API")
    @ApiErrorCodeExamples({
        GlobalErrorCode.USER_AUTHENTICATION_REQUIRED,
        GlobalErrorCode.USER_ADDRESS_NOT_FOUND
    })
    public ApiResponse<Void> setDefaultAddress(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long addressId
    ) {
        addressDefaultService.setDefaultAddress(addressId, userId);
        return ApiResponse.success();
    }
}
