package com.teno.openmarket.user.feature.address.create;

import com.teno.openmarket.core.security.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressCreateApi implements AddressCreateApiDocs {

    private final AddressCreateService addressCreateService;
    private final AddressCreateMapper addressCreateMapper;

    /**
     * 배송지 등록
     * <p>
     * 사용자가 새로운 배송지를 등록합니다.
     * 최대 5개까지 등록 가능하며, 최초 등록 시 자동으로 기본 배송지로 지정됩니다.
     *
     * @param userId {@code SecurityContext}에서 추출된 로그인 사용자의 식별자
     * @param request 등록할 배송지 상세 정보
     * @return {@link ApiResponse} 성공 응답 (데이터 없음)
     */
    @Override
    @PostMapping("/addresses")
    public ApiResponse<Void> createAddress(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid AddressCreateRequest request
    ) {
        AddressCreateCommand command = addressCreateMapper.toCommand(request);
        addressCreateService.createAddress(userId, command);

        return ApiResponse.success();
    }
}
