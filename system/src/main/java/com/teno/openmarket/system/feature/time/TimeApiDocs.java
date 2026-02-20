package com.teno.openmarket.system.feature.time;

import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "System")
public interface TimeApiDocs {

    @Operation(summary = "서버 시간 조회")
    ApiResponse<ServerTimeResponse> getServerTime();
}
