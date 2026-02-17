package com.teno.openmarket.user.infra.redis;

import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * [Refresh Token 리포지토리 어댑터]
 * <p>
 * 도메인 인터페이스({@link RefreshTokenRepository})를 구현하여,
 * 실제 동작을 인프라 리포지토리({@link RefreshTokenRedisRepository})로 위임하는 구현체입니다.
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenRedisRepository redisRepository;

    @Override
    public void save(RefreshToken token) {
        redisRepository.save(token);
    }

    @Override
    public Optional<RefreshToken> findById(Long userId) {
        return redisRepository.findById(userId);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return redisRepository.findByToken(token);
    }

    @Override
    public void delete(RefreshToken token) {
        redisRepository.delete(token);
    }

    @Override
    public void deleteById(Long userId) {
        redisRepository.deleteById(userId);
    }
}
