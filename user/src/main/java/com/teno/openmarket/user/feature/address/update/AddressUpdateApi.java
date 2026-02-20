package com.teno.openmarket.user.feature.address.update;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCodeExamples;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressUpdateApi {

    private final AddressUpdateService addressUpdateService;
    private final AddressUpdateMapper addressUpdateMapper;

    /**
     * 배송지 수정 API
     * <p>
     * 등록된 배송지의 정보를 수정합니다. 기본 배송지 여부({@code isDefault})는 이 API를 통해 변경할 수 없습니다.
     *
     * @param userId SecurityContext에서 추출된 로그인 사용자의 식별자
     * @param addressId 수정할 배송지의 식별자 (경로 변수)
     * @param request 수정할 배송지 상세 정보
     * @return {@link ApiResponse} 성공 응답
     */
    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "배송지 수정 API")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @SecurityErrorCodeExamples(SecurityErrorCode.SECURITY_AUTHENTICATION_REQUIRED)
    @UserErrorCodeExamples(UserErrorCode.USER_ADDRESS_NOT_FOUND)
    public ApiResponse<Void> updateAddress(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long addressId,
        @RequestBody @Valid AddressUpdateRequest request
    ) {
        AddressUpdateCommand command = addressUpdateMapper.toCommand(request);
        addressUpdateService.updateAddress(addressId, userId, command);
        return ApiResponse.success();
    }
}
