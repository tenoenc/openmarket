package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserWithdrawApi {

    private final UserWithdrawFacade userWithdrawFacade;

    /**
     * 회원 탈퇴
     * <p>
     * 현재 로그인한 사용자의 계정을 탈퇴(Soft Delete) 처리합니다.
     * 탈퇴 시 사용 중이던 Access Token은 즉시 블랙리스트 처리되고, Refresh Token은 삭제되어 강제 로그아웃됩니다.
     *
     * @param userId SecurityContext에서 추출된 로그인 사용자의 고유 식별자
     * @param authHeader Authorization 헤더 (Bearer 토큰)
     * @return {@link ApiResponse} 데이터가 없는 성공 응답
     */
    @DeleteMapping("/me")
    @Operation(summary = "회원 탈퇴 API")
    @ApiErrorCodeExamples({
        GlobalErrorCode.USER_NOT_FOUND,
        GlobalErrorCode.USER_AUTHENTICATION_REQUIRED
    })
    public ApiResponse<Void> withdraw(
        @AuthenticationPrincipal Long userId,
        @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.replace("Bearer ", "");

        userWithdrawFacade.withdrawAndLogout(userId, accessToken);

        return ApiResponse.success();
    }
}
