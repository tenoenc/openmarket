package com.teno.openmarket.common.integration;

import com.teno.openmarket.common.security.TokenBlacklistValidator;
import com.teno.openmarket.test.support.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {IntegrationTestCommonSecurityConfig.class})
public abstract class CommonIntegrationTest extends BaseIntegrationTest {

    @MockitoBean
    protected TokenBlacklistValidator tokenBlacklistValidator;

    @BeforeEach
    void setUp() {
        given(tokenBlacklistValidator.existsByAccessToken(any())).willReturn(false);
    }
}
