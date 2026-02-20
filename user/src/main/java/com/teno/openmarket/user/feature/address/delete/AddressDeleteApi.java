package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.core.security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressDeleteApi implements AddressDeleteApiDocs {

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
    @Override
    @DeleteMapping("/addresses/{addressId}")
    public ApiResponse<Void> deletedAddress(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long addressId
    ) {
        addressDeleteService.deleteAddress(addressId, userId);
        return ApiResponse.success();
    }
}
