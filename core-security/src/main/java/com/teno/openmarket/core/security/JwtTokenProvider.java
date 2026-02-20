package com.teno.openmarket.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            parseClaims(token);
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
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 인증 객체(Authentication) 조회
     * <p>
     * 토큰에서 사용자 정보(PK, Role)를 추출하여 SecurityContext에 저장할 Authentication 객체를 생성합니다.
     * <br>
     * 성능을 위해 DB 조회를 생략하고 토큰의 Claims만으로 Principal을 구성합니다.
     *
     * @param token 검증된 JWT Access Token
     * @return SecurityContext에 저장될 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        // 1. 토큰에서 Claims 추출
        Claims claims = parseClaims(token);

        // 2. 권한 정보 추출
        String role = claims.get("role", String.class);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // 3. Long 타입의 principal 추출
        // Principal로 사용할 userId입니다. @AuthenticationPrincipal로 꺼내 쓸 수 있습니다.
        long principal = Long.parseLong(claims.getSubject());

        // 4. Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Access Token 유효 시간 조회
     * <p>
     * 토큰 발급 시 설정된 만료 시간(밀리초 단위)을 반환합니다.
     * 클라이언트에게 만료 시간을 알려주기 위해 사용됩니다.
     *
     * @return Access Token 유효 시간 (ms)
     */
    public Long getAccessTokenValidityInMilliseconds() {
        return accessExp;
    }

    /**
     * Refresh Token 유효 시간 조회
     * <p>
     * Redis에 저장되는 Refresh Token 엔티티의 TTL(Time To Live) 설정을 위해
     * 프로퍼티에 정의된 만료 시간(밀리초)을 반환합니다.
     *
     * @return Refresh Token 유효 시간 (ms)
     */
    public Long getRefreshTokenValidityInMilliseconds() {
        return refreshExp;
    }

    /**
     * 토큰 식별자(Subject) 추출
     * <p>
     * 토큰의 Payload(Claims)를 파싱하여 Subject에 저장된 사용자 고유 ID(PK)를 반환합니다.
     * 내부적으로 서명 검증을 수행하므로, 유효하지 않은 토큰일 경우 예외가 발생합니다.
     *
     * @param token 파싱할 JWT 토큰 문자열
     * @return 토큰에 포함된 사용자 ID (Long)
     * @throws io.jsonwebtoken.JwtException 토큰 파싱 실패 또는 만료 시
     */
    public Long resolveUserId(String token) {
        String subject = parseClaims(token).getSubject();
        return Long.parseLong(subject);
    }

    /**
     * 토큰 권한 정보(Role) 추출
     * <p>
     * 토큰의 Claims에서 커스텀 키("role")에 해당하는 값을 문자열로 추출합니다.
     * 내부적으로 서명 검증을 수행하므로, 유효하지 않은 토큰일 경우 예외가 발생합니다.
     *
     * @param token 파싱할 JWT 토큰 문자열
     * @return 권한 정보 문자열
     * @throws io.jsonwebtoken.JwtException 토큰 파싱 실패 또는 만료 시
     */
    public String resolveRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * 토큰 남은 유효 시간 계산 (ms)
     * <p>
     * 토큰의 Claims에서 Expiration를 추출하고 현재 시간과의 차이를 계산하여 남은 유효 시간을 반환합니다.
     * 내부적으로 서명 검증을 수행하므로, 유효하지 않은 토큰일 경우 예외가 발생합니다.
     *
     * @param token 파싱할 JWT 토큰 문자열
     * @return 남은 유효 시간
     * @throws io.jsonwebtoken.JwtException 토큰 파싱 실패 또는 만료 시
     */
    public Long calculateRemainingValidityInMilliseconds(String token) {
        Date expiration = parseClaims(token).getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
