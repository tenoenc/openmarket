package com.teno.openmarket.user.feature.profile.update;

import com.teno.openmarket.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfileUpdateApi implements ProfileUpdateApiDocs {

    private final ProfileUpdateService profileUpdateService;
    private final ProfileUpdateMapper profileUpdateMapper;

    @Override
    @PutMapping("/me")
    public ApiResponse<ProfileUpdateResponse> updateMyProfile(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ProfileUpdateRequest request
    ) {
        ProfileUpdateCommand command = profileUpdateMapper.toCommand(request);
        ProfileUpdateResponse response = profileUpdateService.updateProfile(userId, command);

        return ApiResponse.success(response);
    }
}
