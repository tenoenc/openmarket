package com.teno.openmarket.user.infra.redis;

import com.teno.openmarket.user.domain.token.BlacklistedToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenBlacklistRedisRepository extends CrudRepository<BlacklistedToken, String> {
}
