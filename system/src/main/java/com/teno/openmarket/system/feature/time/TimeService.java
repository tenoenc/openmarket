package com.teno.openmarket.system.feature.time;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.system.domain.time.TimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeService {

    private final TimeRepository timeRepository;
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * 서버 기준 시간 조회
     * <p>
     * WAS 서버의 로컬 시스템 시간이 아닌, Redis 서버의 현재 시간을 조회하여 반환합니다.
     * 타임딜 오픈, 대기열 진입 등 시간에 민감한 모든 비즈니스 로직의 기준이 됩니다.
     *
     * @return {@link ServerTimeResponse} KST 기준 시간 및 Epoch Timestamp
     * @throws BusinessException Redis 연결 실패 등으로 시간을 가져올 수 없는 경우 (SYSTEM_ERROR)
     */
    public ServerTimeResponse getServerTime() {
        // 1. Timestamp 조회
        long redisTimeMills = timeRepository.getServerTimeMillis();

        // 2. KST 변환 및 DTO 매핑
        LocalDateTime serverTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(redisTimeMills),
            SEOUL_ZONE
        );

        return ServerTimeResponse.builder()
                .serverTime(serverTime)
                .timestamp(redisTimeMills)
                .build();
    }
}
