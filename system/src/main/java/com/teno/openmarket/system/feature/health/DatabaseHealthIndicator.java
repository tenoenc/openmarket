package com.teno.openmarket.system.feature.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements InfrastructureHealthIndicator{

    private final DataSource dataSource;

    @Override
    public String getComponent() {
        return "db";
    }

    @Override
    public boolean isUp() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(3);
            return statement.execute("SELECT 1");
        } catch (Exception e) {
            log.error("[Health] DB Check Failed", e);
            return false;
        }
    }
}
