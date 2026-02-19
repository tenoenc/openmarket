package com.teno.openmarket.test.support;

import com.teno.openmarket.test.config.TestJpaConfig;
import com.teno.openmarket.test.config.TestRedisConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ComponentScan(
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            TestJpaConfig.class,
            TestRedisConfig.class
        }
    )
)
public abstract class BaseIntegrationTest {
}