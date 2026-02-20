package com.teno.openmarket.user.feature.logout;

import com.teno.openmarket.core.security.annotation.SecurityErrorDocs;
import com.teno.openmarket.core.security.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Auth")
@SecurityErrorDocs
public interface LogoutApiDocs {

    @Operation(summary = "로그아웃")
    ApiResponse<Void> logout(HttpServletRequest request);
}
