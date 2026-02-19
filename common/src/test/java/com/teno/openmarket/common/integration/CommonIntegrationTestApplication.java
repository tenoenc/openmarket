package com.teno.openmarket.common.integration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "com.teno.openmarket",
        exclude = {
            // 1. HikariCP 등을 통해 DB 커넥션 풀을 만드려는 시도를 차단
            DataSourceAutoConfiguration.class,
            // 2. TransactionManager를 만드려는 시도를 차단 (DataSource가 없으면 에러 발생)
            DataSourceTransactionManagerAutoConfiguration.class,
            // 3. Hibernate가 SessionFactory를 구성하려는 시도를 차단
            HibernateJpaAutoConfiguration.class
        }
)
public class CommonIntegrationTestApplication {
    public void contextLoads() {

    }
}
