package com.teno.openmarket.user.infra.token;

import com.teno.openmarket.test.support.BaseRedisTest;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(RefreshTokenRepositoryImpl.class)
class RefreshTokenRedisRepositoryTest extends BaseRedisTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("RefreshToken을 저장하고 userId로 조회할 수 있어야 한다")
    void should_SaveAndFind_When_ValidRefreshToken() {
        // given
        Long userId = 1L;
        String tokenValue = "test-refresh-token-value";
        long expiration = 10000L;

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(tokenValue)
                .role("ROLE_USER")
                .expiration(expiration)
                .build();

        // when
        refreshTokenRepository.save(refreshToken);

        // then
        RefreshToken found = refreshTokenRepository.findById(userId).orElseThrow();
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getToken()).isEqualTo(tokenValue);
        assertThat(found.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("@Indexed가 붙은 token 값으로도 조회가 가능해야 한다")
    void should_FindByToken_When_TokenIsIndexed() {
        // given
        Long userId = 2L;
        String tokenValue = "indexed-token-value";
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(tokenValue)
                .expiration(10000L)
                .build();

        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> found = refreshTokenRepository.findByToken(tokenValue);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("저장된 RefreshToken은 설정된 TTL을 가지고 있어야 한다")
    void should_HaveCorrectTtl_When_Saved() {
        // given
        Long userId = 3L;
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token("ttl-check-token")
                .role("ROLE_USER")
                .expiration(14L * 1000) // 이 값과 별개로 @RedisHash 설정을 검증
                .build();

        refreshTokenRepository.save(refreshToken);

        // when
        Long remainingTtl = redisTemplate.getExpire("rt:" + userId);

        // then
        assertThat(remainingTtl).isNotNull();
        // 14초로 설정했으므로, 저장 직후에는 13~14 사이여야 함
        assertThat(remainingTtl).isGreaterThan(13L);
        assertThat(remainingTtl).isLessThanOrEqualTo(14L);
    }
}