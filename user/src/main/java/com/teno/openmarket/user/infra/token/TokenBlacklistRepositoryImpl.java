package com.teno.openmarket.user.infra.token;

import com.teno.openmarket.user.domain.token.BlacklistedToken;
import com.teno.openmarket.user.domain.token.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenBlacklistRepositoryImpl implements TokenBlacklistRepository {

    private final TokenBlacklistRedisRepository redisRepository;

    @Override
    public void save(BlacklistedToken token) {
        redisRepository.save(token);
    }

    @Override
    public boolean existsByAccessToken(String accessToken) {
        return redisRepository.existsById(accessToken);
    }
}
