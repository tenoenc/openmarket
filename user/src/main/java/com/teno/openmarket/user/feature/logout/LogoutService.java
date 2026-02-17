package com.teno.openmarket.user.feature.logout;

import com.teno.openmarket.user.domain.token.BlacklistedToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.domain.token.TokenBlacklistRepository;
import com.teno.openmarket.user.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    /**
     * 로그아웃 처리
     * <ol>
     * <li>Access Token의 남은 유효시간을 계산하여 블랙리스트에 등록 (재사용 방지)</li>
     * <li>해당 사용자의 Refresh Token을 삭제 (재발급 차단)</li>
     * </ol>
     * @param accessToken "Bearer " 접두사가 제거된 순수 토큰 문자열
     */
    @Transactional
    public void logout(String accessToken) {
        // 1. 토큰에서 사용자 ID 추출 및 Refresh Token 삭제
        // (이미 만료된 토큰이라도 블랙리스트 처리를 위해 파싱은 시도해야 함)
        Long userId = jwtTokenProvider.resolveUserId(accessToken);
        refreshTokenRepository.deleteById(userId);

        // 2. 남은 유효 시간 계산
        Long remainingTime = jwtTokenProvider.calculateRemainingValidityInMilliseconds(accessToken);

        // 3. 만료되지 않았다면 블랙리스트에 등록
        if (remainingTime > 0) {
            BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                    .accessToken(accessToken)
                    .expiration(remainingTime)
                    .build();

            tokenBlacklistRepository.save(blacklistedToken);
        }
    }
}
