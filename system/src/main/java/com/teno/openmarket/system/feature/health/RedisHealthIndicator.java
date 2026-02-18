package com.teno.openmarket.system.feature.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHealthIndicator implements InfrastructureHealthIndicator {

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public String getComponent() {
        return "redis";
    }

    @Override
    public boolean isUp() {
        try {
            String pong = redisConnectionFactory.getConnection().ping();
            return "PONG".equalsIgnoreCase(pong);
        } catch (Exception e) {
            log.error("[Health] Redis Check Failed", e);
            return false;
        }
    }
}
