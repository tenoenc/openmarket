package com.teno.openmarket.user.integration;

import com.teno.openmarket.user.domain.user.Role;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationEnvironmentTest extends UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("통합 테스트 환경에서 MySQL과 Redis가 정상적으로 동작해야 한다")
    void should_SaveAndRetrieveDataFromMySqlAndRedis_When_IntegrationIsSetup() {
        // given
        User user = User.builder()
                .email("user@test.com")
                .password("encoded_password")
                .name("integration_tester")
                .phone("010-1234-5678")
                .role(Role.ROLE_USER)
                .build();

        String redisKey = "test:integration:key";
        String redisValue = "integration-value";

        // when
        userRepository.save(user);

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(redisKey, redisValue);

        // then
        User savedUser = userRepository.findByEmail("user@test.com").orElseThrow();

        assertThat(savedUser.getName()).isEqualTo("integration_tester");

        Object savedRedisValue = ops.get(redisKey);
        assertThat(savedRedisValue).isEqualTo(redisValue);
    }
}
