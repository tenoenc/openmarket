package com.teno.openmarket.system.feature.health;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SystemHealthResponse {

    private final String status;
    private final String dbStatus;
    private final String redisStatus;
    private final String uptime;
}
