package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.infra.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReissueServiceTest {

    @InjectMocks
    private ReissueService reissueService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("유효한 Refresh Token으로 요청 시, 기존 토큰을 폐기하고 새 토큰을 발급한다 (RTR)")
    void should_RotateTokens_When_TokenIsValid() {
        // given
        String oldRefreshToken = "old_refresh_token";
        Long userId = 1L;
        String role = "ROLE_USER";

        ReissueCommand command = ReissueCommand.builder().refreshToken(oldRefreshToken).build();

        // 1. 토큰에서 사용자 ID 추출
        given(jwtTokenProvider.validateToken(oldRefreshToken)).willReturn(true);
        given(jwtTokenProvider.resolveUserId(oldRefreshToken)).willReturn(userId);
        given(jwtTokenProvider.resolveRole(oldRefreshToken)).willReturn(role);

        // 2. Redis에 저장된 토큰 조회 (정상 존재)
        RefreshToken storedToken = RefreshToken.builder()
                .userId(userId)
                .token(oldRefreshToken)
                .build();
        given(refreshTokenRepository.findByToken(oldRefreshToken)).willReturn(Optional.of(storedToken));

        // 3. 새 토큰 발급
        given(jwtTokenProvider.createAccessToken(eq(userId), eq(role))).willReturn("new_access_token");
        given(jwtTokenProvider.createRefreshToken(userId)).willReturn("new_refresh_token");
        given(jwtTokenProvider.getAccessTokenValidityInMilliseconds()).willReturn(1800000L);

        // when
        TokenResponse response = reissueService.reissue(command);

        // then
        // 1. 응답 검증
        assertThat(response.getAccessToken()).isEqualTo("new_access_token");
        assertThat(response.getRefreshToken()).isEqualTo("new_refresh_token");

        // 2. 기존 토큰 삭제 검증
        verify(refreshTokenRepository).delete(storedToken);

        // 3. 새 토큰 저장 검증
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Redis에 존재하지 않는 Refresh Token(이미 사용됨/만료됨)으로 요청 시 예외가 발생한다")
    void should_ThrowException_When_TokenNotFoundInRedis() {
        // given
        String oldRefreshToken = "already_used_token";
        ReissueCommand command = ReissueCommand.builder().refreshToken(oldRefreshToken).build();

        given(jwtTokenProvider.validateToken(oldRefreshToken)).willReturn(true);

        // Redis에서 조회되지 않음 (이미 회전되었거나 만료됨)
        given(refreshTokenRepository.findByToken(oldRefreshToken)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reissueService.reissue(command))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.USER_TOKEN_EXPIRED);
    }
}
