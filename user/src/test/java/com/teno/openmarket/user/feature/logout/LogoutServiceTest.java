package com.teno.openmarket.user.feature.logout;

import com.teno.openmarket.user.domain.token.BlacklistedToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.domain.token.TokenBlacklistRepository;
import com.teno.openmarket.common.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Test
    @DisplayName("로그아웃 시 RefreshToken을 삭제하고 AccessToken을 남은 시간만큼 블랙리스트에 등록한다")
    void should_DeleteRefreshTokenAndBlacklistAccessToken_When_Logout() {
        // given
        String accessToken = "valid_access_token";
        Long userId = 100L;
        long remainingTime = 10000L;

        given(jwtTokenProvider.resolveUserId(accessToken)).willReturn(userId);
        given(jwtTokenProvider.calculateRemainingValidityInMilliseconds(accessToken)).willReturn(remainingTime);

        // when
        logoutService.logout(accessToken);

        // then
        // 1. Refresh Token 삭제 검증
        verify(refreshTokenRepository).deleteById(userId);

        // 2. Blacklist 저장 검증
        ArgumentCaptor<BlacklistedToken> captor = ArgumentCaptor.forClass(BlacklistedToken.class);
        verify(tokenBlacklistRepository).save(captor.capture());

        BlacklistedToken savedToken = captor.getValue();
        assertThat(savedToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(savedToken.getExpiration()).isEqualTo(remainingTime);
    }

    @Test
    @DisplayName("이미 만료된 토큰으로 로그아웃 시, RefreshToken은 삭제하지만 블랙리스트에는 저장하지 않는다")
    void should_DeleteRefreshTokenButNotBlacklist_When_TokenIsExpired() {
        // given
        String expiredToken = "expired_access_token";
        Long userId = 100L;
        long remainingTime = -5000L;

        // 만료된 토큰이라도 파싱은 시도해서 userId는 뽑아내야 함
        given(jwtTokenProvider.resolveUserId(expiredToken)).willReturn(userId);
        given(jwtTokenProvider.calculateRemainingValidityInMilliseconds(expiredToken)).willReturn(remainingTime);

        // when
        logoutService.logout(expiredToken);

        // then
        // 1. Refresh Token 삭제는 반드시 수행되어야 함 (재발급 차단)
        verify(refreshTokenRepository).deleteById(userId);

        // 2. 만료된 토큰이므로 블랙리스트 저장은 호출되지 않아야 함
        verify(tokenBlacklistRepository, never()).save(any());
    }
}
