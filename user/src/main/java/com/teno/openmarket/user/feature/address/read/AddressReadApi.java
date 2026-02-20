package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.ListWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressReadApi implements AddressReadApiDocs {

    private final AddressReadService addressReadService;

    /**
     * 내 배송지 목록 조회 API
     * <p>
     * 현재 로그인한 사용자의 전체 배송지 목록을 조회합니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @return {@link ApiResponse} 배송지 정보 목록
     */
    @Override
    @GetMapping("/addresses")
    public ApiResponse<ListWrapper<AddressInfoResponse>> getMyAddresses(
        @AuthenticationPrincipal Long userId
    ) {
        List<AddressInfoResponse> responses = addressReadService.getAddresses(userId);
        return ApiResponse.success(ListWrapper.of(responses));
    }

    /**
     * 내 배송지 상세 조회 API
     * <p>
     * 특정 식별자에 해당하는 배송지 상세 정보를 조회합니다. 본인이 등록한 배송지만 조회 가능합니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @param addressId 조회할 배송지의 식별자 (경로 변수)
     * @return {@link ApiResponse} 배송지 상세 정보
     */
    @Override
    @GetMapping("/addresses/{addressId}")
    public ApiResponse<AddressInfoResponse> getMyAddressDetail(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long addressId
    ) {
        AddressInfoResponse address = addressReadService.getAddress(addressId, userId);
        return ApiResponse.success(address);
    }
}
