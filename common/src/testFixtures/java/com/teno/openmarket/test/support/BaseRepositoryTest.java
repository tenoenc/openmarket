package com.teno.openmarket.test.support;

import com.teno.openmarket.test.config.TestJpaConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest // JPA 관련 빈만 로드
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 사용 금지 (실제 DB 사용)
@Import(TestJpaConfig.class)
public abstract class BaseRepositoryTest {

    // 모든 테스트가 공유하는 하나의 MySQL 컨테이너
    static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");

        MYSQL_CONTAINER.start(); // 컨테이너 시작
    }

    // 동적으로 할당된 컨테이너 포트를 Spring 설정에 바인딩
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // 테스트 매번 초기화
    }
}