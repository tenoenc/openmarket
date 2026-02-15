package com.teno.openmarket.user.infra.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private JwtProperties jwtProperties;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 프로퍼티 설정
        jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey("test-secret-key-for-jwt-token-provider-test-signature\"");
        jwtProperties.setAccessExp(1000 * 60 * 30); // 30분
        jwtProperties.setRefreshExp(1000 * 60 * 60 * 24 * 14); // 2주

        // 2. SecretKey 객체 생성 (검증용)
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        // 3. Provider 주입
        jwtTokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @Test
    @DisplayName("유효한 사용자 ID와 역할이 주어지면 Access Token을 생성해야 한다")
    void should_ReturnAccessToken_When_ValidUserAndRole() {
        // given
        Long userId = 1L;
        String role = "ROLE_USER";

        // when
        String token = jwtTokenProvider.createAccessToken(userId, role);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("유효한 토큰을 검증하면 true를 반환해야 한다")
    void should_ReturnTrue_When_TokenIsValid() {
        // given
        String validToken = Jwts.builder()
                .subject("1")
                .claim("role", "ROLE_USER")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10000)) // 유효 기간 남음
                .signWith(secretKey)
                .compact();

        // when
        boolean isValid = jwtTokenProvider.validateToken(validToken);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰을 검증하면 false를 반환해야 한다")
    void should_ReturnFalse_When_TokenIsExpired() {
        // given
        String expiredToken = Jwts.builder()
                .subject("1")
                .issuedAt(new Date(System.currentTimeMillis() - 10000))
                .expiration(new Date(System.currentTimeMillis() - 1000)) // 이미 만료됨
                .signWith(secretKey)
                .compact();

        // when
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("유효한 토큰에서 사용자 정보(Claims)를 추출해야 한다")
    void should_ReturnClaims_When_TokenIsValid() {
        // given
        Long userId = 1L;
        String role = "ROLE_USER";
        String token = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .signWith(secretKey)
                .compact();

        // when
        Claims claims = jwtTokenProvider.getClaims(token);

        // then
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("role")).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("유효한 토큰으로 인증 객체(Authentication)를 조회해야 한다")
    void should_ReturnAuthentication_When_TokenIsValid() {
        // given
        Long userId = 100L;
        String role = "ROLE_SELLER";
        String token = jwtTokenProvider.createAccessToken(userId, role);

        // when
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // then
        assertThat(authentication).isNotNull();
        // 1. 권한 검증
        assertThat(authentication.getAuthorities()).hasSize(1);
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_SELLER");

        // 2. Principal(사용자 정보) 검증
        Object principal = authentication.getPrincipal();
        assertThat(principal).isInstanceOf(UserDetails.class);
        assertThat(((UserDetails) principal).getUsername()).isEqualTo("100"); // Subject == UserId

    }
}