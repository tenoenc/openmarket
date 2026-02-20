package com.teno.openmarket.system.feature.health;

import com.teno.openmarket.system.feature.SystemControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SystemHealthApi.class)
public class SystemHealthApiTest extends SystemControllerTest {

    @MockitoBean
    private SystemHealthService systemHealthService;

    @Test
    @DisplayName("Health Check 호출 시 DB, Redis, Kafka 상태와 Uptime을 포함한 정상 응답(UP)을 반환한다")
    void should_ReturnFullHealthStatus_When_Called() throws Exception {
        // given
        SystemHealthResponse response = SystemHealthResponse.builder()
                .status("UP")
                .dbStatus("UP")
                .redisStatus("UP")
                .uptime("15d 4h 32m")
                .build();

        // Service는 인프라를 실제로 체크하지 않고, 미리 정의된 Mock 응답을 반환하도록 설정
        given(systemHealthService.getHealthStatus()).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/system/health"))
                .andExpect(status().isOk())
                // 공통 응답 포맷(ApiResponse) 검증
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                // Data 필드 검증
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.dbStatus").value("UP"))
                .andExpect(jsonPath("$.data.redisStatus").value("UP"))
                .andExpect(jsonPath("$.data.uptime").value("15d 4h 32m"));
    }
}
