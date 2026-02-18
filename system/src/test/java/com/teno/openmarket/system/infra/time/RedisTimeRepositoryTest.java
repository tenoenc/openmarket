package com.teno.openmarket.system.infra.time;

import com.teno.openmarket.system.domain.time.TimeRepository;
import com.teno.openmarket.test.support.BaseRedisTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(RedisTimeRepository.class)
public class RedisTimeRepositoryTest extends BaseRedisTest {

    @Autowired
    private TimeRepository timeRepository;

    @Test
    @DisplayName("Redis 서버 시간을 조회하면 유효한 타임스탬프를 반환해야 한다")
    void should_ReturnValidTimestamp_When_ConnectedToRedis() {
        // given
        long startTime = System.currentTimeMillis();

        // when
        long serverTimeMillis = timeRepository.getServerTimeMillis();

        // then
        long endTime = System.currentTimeMillis();

        // 1. 값이 존재하고 양수인지 확인
        assertThat(serverTimeMillis).isPositive();

        // 2. Redis 시간이 테스트 실행 시간대와 유사한지 검증
        // (네트워크 딜레이를 고려하여 앞뒤를 5초 내외의 오차만 허용)
        assertThat(serverTimeMillis).isGreaterThanOrEqualTo(startTime - 5000);
        assertThat(serverTimeMillis).isLessThanOrEqualTo(endTime + 5000);
    }
}
