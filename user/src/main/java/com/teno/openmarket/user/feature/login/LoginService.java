package com.teno.openmarket.user.feature.login;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import com.teno.openmarket.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 로그인 및 인증 토큰 발급 처리
     * <p>
     * 이메일과 비밀번호를 검증하여 사용자를 인증하고, 성공 시 JWT 기반의 Access Token과 Refresh Token을 발급합니다.
     * </p>
     *
     * <ol>
     * <li>자격 증명 검증: 이메일 존재 여부와 비밀번호 일치 여부를 확인합니다.
     * 보안을 위해 실패 사유는 구분하지 않고 {@code USER_LOGIN_FAILED}로 통일합니다.</li>
     * <li>토큰 생성: 인증된 사용자의 ID와 Role을 포함한 Access Token 및 Refresh Token을 생성합니다.</li>
     * <li><strong>RTR (Refresh Token Rotation) 초기화</strong>
     * <ul>
     * <li>발급된 Refresh Token을 저장소(Redis)에 저장하여 상태를 관리합니다.</li>
     * <li>이후 토큰 재발급(Reissue) 시, 저장된 토큰과 대조하여 탈취된 토큰의 재사용을 방지합니다.</li>
     * </ul>
     * </li>
     * </ol>
     *
     * @param command 로그인 요청 명령 DTO (이메일, 평문 비밀번호)
     * @return {@link TokenResponse} Access Token, Refresh Token, 유효 시간 등을 포함한 응답 객체
     * @throws BusinessException 자격 증명이 유효하지 않은 경우 ({@link UserErrorCode#USER_LOGIN_FAILED})
     */
    @Transactional
    public TokenResponse login(LoginCommand command) {
        // 1. 사용자 조회
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_LOGIN_FAILED));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new BusinessException(UserErrorCode.USER_LOGIN_FAILED);
        }

        // 3. 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshTokenValue = jwtTokenProvider.createRefreshToken(user.getId());

        Long accessExpiresIn = jwtTokenProvider.getAccessTokenValidityInMilliseconds();
        Long refreshExpiresIn = jwtTokenProvider.getRefreshTokenValidityInMilliseconds();

        // 4. Refresh Token 저장 (RTR)
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshTokenValue)
                .role(user.getRole().name())
                .expiration(refreshExpiresIn)
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 응답 생성
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .expiresIn(accessExpiresIn)
                .build();
    }
}
