package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@SecurityErrorDocs
public interface UserWithdrawApiDocs {

    @Operation(summary = "회원 탈퇴 API")
    @UserErrorCodeExamples(UserErrorCode.USER_NOT_FOUND)
    ApiResponse<Void> withdraw(Long userId, String authHeader);
}
