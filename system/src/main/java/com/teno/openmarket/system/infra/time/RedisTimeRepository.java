package com.teno.openmarket.system.infra.time;

import com.teno.openmarket.system.domain.time.TimeRepository;
import com.teno.openmarket.system.domain.time.TimeSynchronizationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisTimeRepository implements TimeRepository {

    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public long getServerTimeMillis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            Long redisTimeMillis = connection.serverCommands().time();

            if (redisTimeMillis == null) {
                throw new TimeSynchronizationException("Redis time command returned null");
            }

            return redisTimeMillis;
        } catch (RedisSystemException e) {
            log.error("Failed to execute TIME command", e);
            throw new TimeSynchronizationException("Failed to fetch Redis server time", e);
        }
    }
}
