package com.teno.openmarket.user.feature.profile.update;

import com.teno.openmarket.common.annotation.ApiErrorCodeExamples;
import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileUpdateApi {

    private final ProfileUpdateService profileUpdateService;
    private final ProfileUpdateMapper profileUpdateMapper;

    @PutMapping("/me")
    @Operation(summary = "내 정보 수정 API")
    @ApiErrorCodeExamples({
        GlobalErrorCode.SYSTEM_INVALID_INPUT,
        GlobalErrorCode.USER_NOT_FOUND,
        GlobalErrorCode.USER_AUTHENTICATION_REQUIRED
    })
    public ApiResponse<ProfileUpdateResponse> updateMyProfile(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ProfileUpdateRequest request
    ) {
        ProfileUpdateCommand command = profileUpdateMapper.toCommand(request);
        ProfileUpdateResponse response = profileUpdateService.updateProfile(userId, command);

        return ApiResponse.success(response);
    }
}
