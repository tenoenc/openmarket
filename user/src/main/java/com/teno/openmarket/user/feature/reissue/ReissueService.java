package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.infra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse reissue(ReissueCommand command) {
        String requestToken = command.getRefreshToken();

        // 1. 토큰 자체의 유효성 검사 (서명, 만료 여부 등)
        if (!jwtTokenProvider.validateToken(requestToken)) {
            throw new BusinessException(GlobalErrorCode.USER_TOKEN_EXPIRED);
        }

        // 2. Redis에 저장된 토큰인지 조회
        // 조회되지 않는다면? -> 이미 사용되었거나(RTR), 만료되어 삭제된 토큰 -> 재사용 공격 의심
        RefreshToken storedToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_TOKEN_EXPIRED));

        // 3. 기존 토큰 폐기 (1회용 사용 보장)
        refreshTokenRepository.delete(storedToken);

        // 4. 정보 추출 및 신규 토큰 생성
        Long userId = jwtTokenProvider.resolveUserId(requestToken);
        String role = jwtTokenProvider.resolveRole(requestToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userId, role);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        Long expiresIn = jwtTokenProvider.getAccessTokenValidityInMilliseconds();

        // 5. 신규 Refresh Token 저장
        RefreshToken newToken = RefreshToken.builder()
                .userId(userId)
                .token(newRefreshToken)
                .build();

        refreshTokenRepository.save(newToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(expiresIn)
                .build();
    }
}
