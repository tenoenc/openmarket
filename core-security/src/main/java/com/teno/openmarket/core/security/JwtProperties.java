package com.teno.openmarket.core.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * HS256 서명용 Secret Key (32 bytes 이상 필수)
     */
    private String secretKey;

    /**
     * Access Token 만료 시간 (ms) - 기본 30분
     */
    private long accessExp;

    /**
     * Refresh Token 만료 시간 (ms) - 기본 2주 (RTR 적용 대상)
     */
    private long refreshExp;
}
