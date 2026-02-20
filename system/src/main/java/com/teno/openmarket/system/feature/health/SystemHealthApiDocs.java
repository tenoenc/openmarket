package com.teno.openmarket.system.feature.health;

import com.teno.openmarket.core.security.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "System", description = "시스템 관리 API")
public interface SystemHealthApiDocs {

    @Operation(summary = "서버 상태 확인 (Health Check)")
    ApiResponse<SystemHealthResponse> checkHealth();
}
