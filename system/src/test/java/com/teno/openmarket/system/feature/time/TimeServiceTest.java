package com.teno.openmarket.system.feature.time;

import com.teno.openmarket.system.domain.time.TimeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTest {

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeService timeService;

    @Test
    @DisplayName("Redis 서버 시간을 조회하여 한국 시간(KST)으로 반환해야 한다")
    void should_ReturnRedisTimeInKst_When_ServerTimeIsRequested() {
        // given
        long fakeEpochMillis = 1708214400000L; // 2024-02-18 09:00:00 (UTC) -> 18:00:00 (KST)

        given(timeRepository.getServerTimeMillis()).willReturn(fakeEpochMillis);

        // when
        ServerTimeResponse response = timeService.getServerTime();

        // then
        assertThat(response.getTimestamp()).isEqualTo(fakeEpochMillis);

        ZonedDateTime expectedKstTime = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(fakeEpochMillis),
                ZoneId.of("Asia/Seoul")
        );

        assertThat(response.getServerTime()).isEqualTo(expectedKstTime.toLocalDateTime());
    }

    @Test
    @DisplayName("서버 로컬 시간이 현재여도, Redis가 반환하는 과거/미래 시간을 정확히 따라야 한다")
    void should_IgnoreSystemTimeAndFollowRedisTime_When_ServerTimeIsRequested() {
        // given
        long pastEpochMillis = 946684800000L; // 2000-01-01 00:00:00 (KST)

        given(timeRepository.getServerTimeMillis()).willReturn(pastEpochMillis);

        // when
        ServerTimeResponse response = timeService.getServerTime();

        // then
        assertThat(response.getServerTime().getYear()).isEqualTo(2000);

        // then
        assertThat(response.getServerTime()).isNotEqualTo(LocalDateTime.now());
    }
}
