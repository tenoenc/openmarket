package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.core.security.exception.SecurityErrorCode;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    /**
     * 인증 토큰 재발급 처리
     * <p>
     * 만료된 Access Token을 대신하여, 유효한 Refresh Token을 검증하고 새로운 토큰 쌍을 발급합니다.
     * </p>
     *
     * <strong>RTR (Refresh Token Rotation) 정책</strong>
     * <ol>
     * <li>토큰 검증: 전달받은 Refresh Token의 서명 유효성 및 만료 여부를 1차 검증합니다.</li>
     * <li>저장소 대조: Redis에 저장된 토큰과 일치하는지 확인합니다.
     * <ul>
     * <li>저장소에 없거나 값이 다르다면, 이미 사용된 토큰(탈취 시도)이거나 만료된 토큰으로 간주하여 예외를 발생시킵니다.</li>
     * </ul>
     * </li>
     * <li><strong>1회용 폐기:</strong> 사용된 기존 Refresh Token은 즉시 삭제하여 재사용을 원천 차단합니다.</li>
     * <li>신규 발급: 새로운 Access/Refresh Token을 생성하고, 새 Refresh Token을 저장소에 저장합니다.</li>
     * </ol>
     *
     * @param command 재발급 요청 정보를 담은 커맨드 객체
     * @return {@link TokenResponse} 갱신된 Access Token, Refresh Token 및 유효 시간을 포함한 응답
     * @throws BusinessException 토큰이 유효하지 않거나, 이미 사용된 경우 ({@link SecurityErrorCode#SECURITY_TOKEN_EXPIRED})
     */
    @Transactional
    public TokenResponse reissue(ReissueCommand command) {
        String requestToken = command.getRefreshToken();

        // 1. 토큰 자체의 유효성 검사 (서명, 만료 여부 등)
        if (!jwtTokenProvider.validateToken(requestToken)) {
            throw new BusinessException(SecurityErrorCode.SECURITY_TOKEN_EXPIRED);
        }

        // 2. Redis에 저장된 토큰인지 조회
        // 조회되지 않는다면? -> 이미 사용되었거나(RTR), 만료되어 삭제된 토큰 -> 재사용 공격 의심
        RefreshToken storedToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new BusinessException(SecurityErrorCode.SECURITY_TOKEN_EXPIRED));

        // 3. 기존 토큰 폐기 (1회용 사용 보장)
        refreshTokenRepository.delete(storedToken);

        // 4. 정보 추출 및 신규 토큰 생성
        Long userId = jwtTokenProvider.resolveUserId(requestToken);
        String role = storedToken.getRole();

        String newAccessToken = jwtTokenProvider.createAccessToken(userId, role);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        Long accessExpiresIn = jwtTokenProvider.getAccessTokenValidityInMilliseconds();
        Long refreshExpiresIn = jwtTokenProvider.getRefreshTokenValidityInMilliseconds();

        // 5. 신규 Refresh Token 저장
        RefreshToken newToken = RefreshToken.builder()
                .userId(userId)
                .token(newRefreshToken)
                .role(role)
                .expiration(refreshExpiresIn)
                .build();

        refreshTokenRepository.save(newToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(accessExpiresIn)
                .build();
    }
}
