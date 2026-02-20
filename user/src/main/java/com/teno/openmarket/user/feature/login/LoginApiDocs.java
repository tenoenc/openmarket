package com.teno.openmarket.user.feature.login;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth")
public interface LoginApiDocs {

    @Operation(summary = "로그인 요청")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @UserErrorCodeExamples(UserErrorCode.USER_LOGIN_FAILED)
    ApiResponse<TokenResponse> login(LoginRequest request);
}
