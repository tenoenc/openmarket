package com.teno.openmarket.user.infra.token;

import com.teno.openmarket.user.domain.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * [Refresh Token 인프라 리포지토리]
 * <p>
 * Spring Data Redis 기술을 사용하여 실제 데이터 스토어(Redis)와 통신하는 인터페이스입니다.
 * </p>
 * <strong>주의사항</strong>
 * <ul>
 * <li>도메인 서비스 로직에서는 이 클래스를 직접 의존(Import)하면 안 됩니다.</li>
 * <li>오직 어댑터 클래스({@link RefreshTokenRepositoryImpl}) 내부에서만 사용되어야 합니다.</li>
 * </ul>
 */
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
}
