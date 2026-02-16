package com.teno.openmarket.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.teno.openmarket")
@EnableJpaRepositories(basePackages = "com.teno.openmarket")
public class JpaConfig {
}
