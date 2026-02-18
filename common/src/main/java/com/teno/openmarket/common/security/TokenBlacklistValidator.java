package com.teno.openmarket.common.security;

public interface TokenBlacklistValidator {
    boolean existsByAccessToken(String token);
}
