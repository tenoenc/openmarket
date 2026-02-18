package com.teno.openmarket.test.support;

import com.teno.openmarket.test.config.TestRedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@DataRedisTest
@Testcontainers
@Import(TestRedisConfig.class)
public abstract class BaseRedisTest {

    // Redis 컨테이너 실행 (Docker 필수)
    @Container
    static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
                    .withExposedPorts(6379);

    // 동적으로 할당된 컨테이너 포트를 Spring 설정에 바인딩
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> String.valueOf(REDIS_CONTAINER.getFirstMappedPort()));
    }

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
}
