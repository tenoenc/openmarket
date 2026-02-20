package com.teno.openmarket.user.feature.signup;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.response.ApiResponse;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.exception.UserErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "회원 인증 및 인가 API")
public interface SignupApiDocs {

    @Operation(summary = "회원 가입 요청")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @UserErrorCodeExamples({
        UserErrorCode.USER_ALREADY_EXISTS,
        UserErrorCode.USER_TERMS_REQUIRED
    })
    ApiResponse<Void> signup(@RequestBody @Valid SignupRequest request);
}
