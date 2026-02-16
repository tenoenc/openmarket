package com.teno.openmarket.user.feature.auth.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

/**
 * Refresh Token Redis 엔티티
 * <p>
 * JWT 인증 방식에서 Access Token 갱신을 위해 사용되는 Refresh Token을 저장합니다.
 * <br>
 * <b>정책</b>
 * <ul>
*  <li>Key 전략: "rt:{userId}" (Spring Data Redis의 {@code @RedisHash} Prefix + {@code @Id})</li>
 * <li>1인 1기기 정책: 동일한 userId로 새로운 토큰이 저장되면 기존 토큰은 덮어씌워짐(무효화).</li>
 * <li>TTL: 설정된 만료 시간(refresh-exp)이 지나면 Redis에서 자동 삭제됨.</li>
 * </ul>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "rt")
public class RefreshToken {

    /**
     * 사용자 고유 식별자 (Redis Key의 ID 부분)
     * <p>
     * 예: userId가 100일 경우, Redis Key는 "rt:100"이 됩니다.
     */
    @Id
    private Long userId;

    /**
     * 실제 Refresh Token 문자열
     * <p>
     * {@code @Indexed} 어노테이션을 통해 값으로 조회가 가능합니다.
     * (예: 토큰 재발급 요청 시 토큰 값으로 해당 엔티티 찾을 때 사용)
     */
    @Indexed
    private String token;

    /**
     * 사용자 권한 정보
     * <p>
     * Access Token 재발급 시 DB 조회 비용을 줄이기 위해 캐싱합니다.
     */
    private String role;

    /**
     * 데이터 만료 시간 (TTL)
     * <p>
     * 해당 시간이 지나면 Redis에서 데이터가 자동 소멸됩니다. (단위: 밀리초)
     */
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;
}
