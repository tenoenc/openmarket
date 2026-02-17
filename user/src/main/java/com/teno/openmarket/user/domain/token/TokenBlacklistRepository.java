package com.teno.openmarket.user.domain.token;

public interface TokenBlacklistRepository {

    void save(BlacklistedToken token);

    boolean existsByAccessToken(String accessToken);
}
