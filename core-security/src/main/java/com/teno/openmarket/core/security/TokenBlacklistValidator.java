package com.teno.openmarket.core.security;

public interface TokenBlacklistValidator {
    boolean existsByAccessToken(String token);
}
