package com.teno.openmarket.system.feature.time;

import com.teno.openmarket.system.feature.SystemControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TimeApi.class)
public class TimeApiTest extends SystemControllerTest {

    @MockitoBean
    private TimeService timeService;

    @Test
    @DisplayName("서버 시간을 요청하면 포맷팅된 시간과 타임스탬프를 반환해야 한다")
    void should_ReturnFormattedServerTime_When_ApiIsCalled() throws Exception {
        // given
        LocalDateTime mockTime =
                LocalDateTime.of(2026, 2, 18, 10, 0, 0, 123000000);// 10:00:00.123
        ServerTimeResponse response = ServerTimeResponse.builder()
                .serverTime(mockTime)
                .timestamp(1708218000123L)
                .build();

        given(timeService.getServerTime()).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/system/server-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.serverTime").value("2026-02-18 10:00:00.123"))
                .andExpect(jsonPath("$.data.timestamp").value(1708218000123L));
    }
}
