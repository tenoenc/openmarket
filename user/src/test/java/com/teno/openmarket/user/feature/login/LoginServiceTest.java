package com.teno.openmarket.user.feature.login;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.common.security.JwtTokenProvider;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.token.RefreshToken;
import com.teno.openmarket.user.domain.token.RefreshTokenRepository;
import com.teno.openmarket.user.domain.user.Role;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 예외가 발생해야 한다")
    void should_ThrowException_When_EmailNotFound() {
        // given
        LoginCommand command = LoginCommand.builder()
                .email("unknown@teno.com")
                .password("password")
                .build();
        given(userRepository.findByEmail(command.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loginService.login(command))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_LOGIN_FAILED);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생해야 한다")
    void should_ThrowException_When_PasswordMismatch() {
        // given
        LoginCommand command = LoginCommand.builder()
                .email("test@teno.com")
                .password("wrong_password")
                .build();
        User user = User.builder()
                .email("test@teno.com")
                .password("encoded_password")
                .build();

        given(userRepository.findByEmail(command.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(command.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> loginService.login(command))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_LOGIN_FAILED);
    }

    @Test
    @DisplayName("로그인 성공 시 AccessToken과 RefreshToken이 발급되고, 저장소에 만룍시간과 권한이 올바르게 저장된다")
    void should_ReturnTokensAndSaveRefreshToken_When_LoginSuccessful() {
        // given
        LoginCommand command = LoginCommand.builder()
                .email("test@teno.com")
                .password("correct_password")
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@teno.com")
                .password("encoded_password")
                .role(Role.ROLE_USER)
                .build();

        given(userRepository.findByEmail(command.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(command.getPassword(), user.getPassword())).willReturn(true);

        given(jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name())).willReturn("access_token");
        given(jwtTokenProvider.createRefreshToken(user.getId())).willReturn("refresh_token");

        given(jwtTokenProvider.getRefreshTokenValidityInMilliseconds())
                .willReturn(1209600000L); // 14일

        // when
        TokenResponse response = loginService.login(command);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access_token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh_token");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken savedToken = captor.getValue();
        assertThat(savedToken.getUserId()).isEqualTo(user.getId());
        assertThat(savedToken.getToken()).isEqualTo("refresh_token");
        assertThat(savedToken.getRole()).isEqualTo("ROLE_USER");
        assertThat(savedToken.getExpiration()).isEqualTo(1209600000L);

    }
}
