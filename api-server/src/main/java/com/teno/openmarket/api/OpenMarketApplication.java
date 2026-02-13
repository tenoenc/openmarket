package com.teno.openmarket.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.teno", exclude = {DataSourceAutoConfiguration.class})
public class OpenMarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenMarketApplication.class, args);
    }
}
