package com.teno.openmarket.user.feature.profile.info;

import com.teno.openmarket.core.security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileInfoApi implements ProfileInfoApiDocs {

    private final ProfileInfoService profileInfoService;

    /**
     * 내 정보 조회
     * <p>
     * 현재 로그인한 사용자의 프로필(회원) 정보를 조회합니다.
     * SecurityContext에 저장된 인증 객체(userId)를 기반으로 사용자 데이터를 반환합니다.
     *
     * @param userId SecurityContext에서 추출된 로그인 사용자의 고유 식별자
     * @return {@link ApiResponse} 사용자의 프로필 상세 정보
     */
    @Override
    @GetMapping("/me")
    public ApiResponse<ProfileInfoResponse> getMyProfile(
        @AuthenticationPrincipal Long userId
    ) {
        ProfileInfoResponse response = profileInfoService.getProfile(userId);
        return ApiResponse.success(response);
    }
}
