package com.teno.openmarket.core.security.integration;

import com.teno.openmarket.core.security.TokenBlacklistValidator;
import com.teno.openmarket.test.support.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {IntegrationTestCoreSecurityConfig.class})
public abstract class CoreSecurityIntegrationTest extends BaseIntegrationTest {

    @MockitoBean
    protected TokenBlacklistValidator tokenBlacklistValidator;

    @BeforeEach
    void setUp() {
        given(tokenBlacklistValidator.existsByAccessToken(any())).willReturn(false);
    }
}
