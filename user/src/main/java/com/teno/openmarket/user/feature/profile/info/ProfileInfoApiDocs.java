package com.teno.openmarket.user.feature.profile.info;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "회원 관리 API")
@SecurityErrorDocs
public interface ProfileInfoApiDocs {

    @Operation(summary = "내 정보 조회 API")
    @UserErrorCodeExamples(UserErrorCode.USER_NOT_FOUND)
    ApiResponse<ProfileInfoResponse> getMyProfile(Long userId);
}
