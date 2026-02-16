package com.teno.openmarket.user.infra.redis.repository;

import com.teno.openmarket.user.feature.auth.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Refresh Token Redis 저장소 인터페이스
 * <p>
 * Spring Data Redis를 사용하여 {@link RefreshToken} 엔티티의 CRUD를 담당합니다.
 */
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    /**
     * 토큰 값으로 Refresh Token 엔티티 조회
     * <p>
     * {@link RefreshToken#getToken()} 필드에 걸린 {@code @Indexed}를 활용하여 조회합니다.
     * RTR(Refresh Token Rotation) 검증 시 사용됩니다.
     *
     * @param token 클라이언트로부터 전달받은 Refresh Token 문자열
     * @return 해당 토큰을 가진 RefreshToken 엔티티 (존재하지 않으면 Empty)
     */
    Optional<RefreshToken> findByToken(String token);
}
