package com.teno.openmarket.user.infra.security;

import com.teno.openmarket.core.security.TokenBlacklistValidator;
import com.teno.openmarket.user.domain.token.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlacklistValidatorAdapter implements TokenBlacklistValidator {

    private final TokenBlacklistRepository repository;

    @Override
    public boolean existsByAccessToken(String token) {
        return repository.existsByAccessToken(token);
    }
}
