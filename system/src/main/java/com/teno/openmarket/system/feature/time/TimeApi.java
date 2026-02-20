package com.teno.openmarket.system.feature.time;

import com.teno.openmarket.core.security.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System")
@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class TimeApi {

    private final TimeService timeService;

    /**
     * 서버 기준 시간 조회
     * <p>
     * 타임딜의 공정성을 위해 WAS 로컬 시간이 아닌, Redis 기준의 통합 서버 시간을 반환합니다.
     * 클라이언트는 이 API를 폴링하거나 초기 접속 시 호출하여
     * 로컬 시간과의 오차(Clock Drift)를 보정해야 합니다.
     *
     * @return {@link ServerTimeResponse} 서버 시간(KST) 및 타임스탬프
     */
    @GetMapping("/server-time")
    @Operation(summary = "서버 시간 조회")
    public ApiResponse<ServerTimeResponse> getServerTime() {
        return ApiResponse.success(timeService.getServerTime());
    }
}
