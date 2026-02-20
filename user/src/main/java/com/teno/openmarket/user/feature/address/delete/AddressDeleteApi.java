package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [배송지 삭제 API 컨트롤러]
 * <p>
 * 클라이언트가 등록된 배송지를 삭제할 수 있는 REST API 엔드포인트를 제공합니다.
 */
@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressDeleteApi {

    private final AddressDeleteService addressDeleteService;

    /**
     * 배송지 삭제 API
     * <p>
     * 등록된 배송지를 삭제합니다. 단, 기본 배송지({@code isDefault=true})로 설정된 항목은 삭제할 수 없습니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @param addressId 삭제할 배송지의 식별자 (경로 변수)
     * @return {@link ApiResponse} 성공 응답
     */
    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "배송지 삭제 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SECURITY_AUTHENTICATION_REQUIRED)
    @UserErrorCodeExamples({
        UserErrorCode.USER_ADDRESS_NOT_FOUND,
        UserErrorCode.USER_ADDRESS_DEFAULT
    })
    public ApiResponse<Void> deletedAddress(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long addressId
    ) {
        addressDeleteService.deleteAddress(addressId, userId);
        return ApiResponse.success();
    }
}
