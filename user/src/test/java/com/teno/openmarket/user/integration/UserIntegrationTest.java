package com.teno.openmarket.user.integration;

import com.teno.openmarket.test.support.BaseIntegrationTest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Set;

@Transactional
@ContextConfiguration(classes = {IntegrationTestRedisConfig.class, IntegrationTestJpaConfig.class})
public abstract class UserIntegrationTest extends BaseIntegrationTest {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:3.2.0");

    static final MySQLContainer<?> MYSQL_CONTAINER;
    static final GenericContainer<?> REDIS_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE))
                .withDatabaseName("openmarket_test")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);

        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(6379)
                .withReuse(true);

        MYSQL_CONTAINER.start();
        REDIS_CONTAINER.start();

        // 컨테이너 실행 직후 Flyway 마이그레이션 강제 실행
        // api-server 모듈의 경로를 상대 경로로 지정해야 함
        Flyway flyway = Flyway.configure()
                .dataSource(MYSQL_CONTAINER.getJdbcUrl(), MYSQL_CONTAINER.getUsername(), MYSQL_CONTAINER.getPassword())
                .locations("filesystem:../api-server/src/main/resources/db/migration")
                .load();

        flyway.migrate();
    }

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // MySQL 설정 바인딩
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);

        // Redis 설정 바인딩
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    /**
     * 각 테스트 메서드 실행 후 Redis 데이터를 초기화합니다.
     * MySQL은 @Transactional에 의해 자동으로 롤백되지만, Redis는 명시적 초기화가 필요합니다.
     */
    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
