package com.teno.openmarket.user.domain.token;

import java.util.Optional;

/**
 * [Refresh Token 도메인 리포지토리 인터페이스]
 * <p>
 * 도메인 영역에서 Refresh Token을 저장하고 조회하기 위한 추상화된 접근 지점(Port)입니다.
 */
public interface RefreshTokenRepository {

    void save(RefreshToken token);

    Optional<RefreshToken> findById(Long userId);

    /**
     * 토큰 값으로 Refresh Token 엔티티 조회
     * <p>
     * {@link RefreshToken#getToken()} 필드에 걸린 {@code @Indexed}를 활용하여 조회합니다.
     * RTR(Refresh Token Rotation) 검증 시 사용됩니다.
     *
     * @param token 클라이언트로부터 전달받은 Refresh Token 문자열
     * @return 해당 토큰을 가진 RefreshToken 엔티티 (존재하지 않으면 Empty)
     */
    Optional<RefreshToken> findByToken(String token);

    void delete(RefreshToken token);

    void deleteById(Long userId);
}
