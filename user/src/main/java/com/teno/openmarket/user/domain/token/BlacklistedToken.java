package com.teno.openmarket.user.domain.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

/**
 * [Blacklisted Token Redis 엔티티]
 * <p>
 * 로그아웃 등으로 인해 무효화된 Access Token을 저장 관리합니다.
 * JWT는 Stateless하므로 서버측에서 세션을 즉시 만료시킬 수 없기에,
 * 남은 유효 시간 동안 블랙리스트에 등록하여 재사용을 차단합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "bl")
public class BlacklistedToken {

    /**
     * 무효화할 Access Token 문자열 (PK)
     * <p>
     * 토큰 문자열 자체가 Redis Key의 ID로 사용됩니다.
     */
    @Id
    private String accessToken;

    /**
     * 상태 값 (예: "LOGOUT")
     * <p>
     * 키의 존재 여부가 중요하므로 값 자체는 단순 식별 용도입니다.
     */
    @Builder.Default
    private String status = "LOGOUT";

    /**
     * 데이터 만료 시간 (TTL)
     * <p>
     * Access Token의 남은 유효 시간(ms)으로 설정됩니다.
     * 토큰 자체가 만료되면 블랙리스트에서도 유지할 필요가 없으므로 자동 소멸됩니다.
     */
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;
}