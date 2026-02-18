package com.teno.openmarket.system.feature.health;

import com.teno.openmarket.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System", description = "시스템 관리 API")
@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class SystemHealthApi {

    private final SystemHealthService systemHealthService;

    /**
     * 서버 상태 확인 (Health Check)
     * <p>
     * 로드밸런서(AWS ALB, L4)나 모니터링 시스템이 서버의 생존 여부를 확인하기 위해 호출하는 엔드포인트입니다.
     *
     * <ul>
     * <li>인증 필터(Security)를 거치지 않고 접근 가능해야 합니다. (permitAll)</li>
     * <li>DB나 Redis에 직접 커넥션을 맺지 않고, 비동기 스레드가 캐싱해둔 상태 값을 즉시 반환하여 응답 지연을 방지합니다.</li>
     * </ul>
     *
     * @return {@link ApiResponse} 시스템의 통합 상태(UP/DOWN) 및 각 인프라별 연결 상태(DB, Redis, Kafka)
     */
    @GetMapping("/health")
    @Operation(summary = "서버 상태 확인 (Health Check)")
    public ApiResponse<SystemHealthResponse> checkHealth() {
        // 캐싱된 결과를 즉시 반환
        return ApiResponse.success(systemHealthService.getHealthStatus());
    }
}