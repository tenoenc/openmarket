package com.teno.openmarket.system.feature.health;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemHealthService {

    // 인터페이스 목록 주입
    private final List<InfrastructureHealthIndicator> indicators;

    private final AtomicReference<SystemHealthResponse> cachedHealth = new AtomicReference<>();

    @PostConstruct
    public SystemHealthResponse getHealthStatus() {
        return cachedHealth.get();
    }

    @Scheduled(fixedRate = 10000)
    public void refreshHealthStatus() {
        // 1. 모든 인디케이터 순회하며 상태 점검
        Map<String, Boolean> results = indicators.stream()
                .collect(Collectors.toMap(
                    InfrastructureHealthIndicator::getComponent,
                    InfrastructureHealthIndicator::isUp
                ));

        // 2. 전체 상태 판단 (하나라도 false면 Down)
        boolean isOverallUp = results.values().stream().allMatch(Boolean::booleanValue);

        // 3. 응답 객체 생성 (Map에서 꺼내서 바인딩)
        SystemHealthResponse response = SystemHealthResponse.builder()
                .status(isOverallUp ? "UP" : "DOWN")
                .dbStatus(toStatusString(results.getOrDefault("db", false)))
                .redisStatus(toStatusString(results.getOrDefault("redis", false)))
                .uptime(getUptimeString())
                .build();

        cachedHealth.set(response);

        if (!isOverallUp) {
            log.warn("[Health] System Unstable: {}", results);
        }
    }

    private String toStatusString(boolean isUp) {
        return isUp ? "UP" : "DOWN";
    }

    private String getUptimeString() {
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration duration = Duration.ofMillis(uptimeMillis);
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        return String.format("%dd %dh %dm", days, hours, minutes);
    }
}
