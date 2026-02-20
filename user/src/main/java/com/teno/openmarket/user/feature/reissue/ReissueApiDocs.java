package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.core.security.annotation.GlobalErrorCodeExamples;
import com.teno.openmarket.core.security.error.GlobalErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCode;
import com.teno.openmarket.core.security.exception.SecurityErrorCodeExamples;
import com.teno.openmarket.core.security.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth")
public interface ReissueApiDocs {

    @Operation(summary = "토큰 재발급 요청")
    @GlobalErrorCodeExamples(GlobalErrorCode.SYSTEM_INVALID_INPUT)
    @SecurityErrorCodeExamples(SecurityErrorCode.SECURITY_TOKEN_EXPIRED)
    ApiResponse<TokenResponse> reissue(ReissueRequest request);
}
