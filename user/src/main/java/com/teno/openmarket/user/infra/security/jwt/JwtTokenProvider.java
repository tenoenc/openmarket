package com.teno.openmarket.user.infra.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT(Json Web Token) 생성 및 검증 공급자
 * <p>
 * JJWT 라이브러리를 사용하여 Access Token과 Refresh Token을 발급하고,
 * 서명 검증 및 Claims 추출을 수행하는 인프라 컴포넌트입니다.
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessExp;
    private final long refreshExp;

    /**
     * @param jwtProperties JWT 설정 프로퍼티 (SecretKey, Expiration)
     */
    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.accessExp = jwtProperties.getAccessExp();
        this.refreshExp = jwtProperties.getRefreshExp();
    }

    /**
     * Access Token 생성
     * <p>
     * 인증된 사용자의 식별자와 권한 정보를 포함하는 짧은 수명의 토큰을 생성합니다.
     *
     * @param userId 사용자 고유 식별자 (Subject)
     * @param role 사용자 권한 (ex: ROLE_USER)
     * @return 서명된 JWT Access Token 문자열
     */
    public String createAccessToken(Long userId, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExp);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token 생성
     * <p>
     * Access Token 갱신을 위한 긴 수명의 토큰을 생성합니다.
     * <br>
     * 보안을 위해 최소한(UserId)의 정보만 포함하며, 권한 정보 등은 제외합니다.
     *
     * @param userId 사용자 고유 식별자 (Subject)
     * @return 서명된 JWT Refresh Token 문자열
     */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExp);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     * <p>
     * 토큰의 서명, 만료 여부, 구조 등을 검증합니다.
     * 검증 실패 시 로그를 남기고 false를 반환합니다.
     *
     * @param token 검증할 JWT 토큰 문자열
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException exception) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * Claims 추출
     * <p>
     * 토큰을 파싱하여 내부의 Payload(Claims)를 반환합니다.
     *
     * @param token 파싱할 Jwt 토큰
     * @return 추출된 Claims 객체
     * @throws JwtException 토큰이 유효하지 않거나 만료된 경우
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
