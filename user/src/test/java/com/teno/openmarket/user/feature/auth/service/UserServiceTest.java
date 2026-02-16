package com.teno.openmarket.user.feature.auth.service;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.entity.User;
import com.teno.openmarket.user.domain.repository.UserRepository;
import com.teno.openmarket.user.domain.vo.Role;
import com.teno.openmarket.user.feature.auth.dto.SignupCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유효한 회원가입 명령이 들어오면 비밀번호를 암호화하고 유저를 저장해야 한다")
    void should_EncryptPasswordAndSaveUser_When_SignupCommandIsValid() {
        // given
        SignupCommand command = SignupCommand.builder()
                .email("test@teno.com")
                .password("Password123!")
                .name("테스터")
                .phone("010-1234-5678")
                .termIds(List.of(1L, 2L))
                .build();

        given(userRepository.existsByEmail(command.getEmail())).willReturn(false);
        given(passwordEncoder.encode(command.getPassword())).willReturn("encoded_password_value");
        given(userRepository.save(any(User.class)))
                .willAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    return user;
                });

        // when
        signupService.signup(command);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture()); // save()가 호출되는 시점에 해당 객체를 낚아챔

        User savedUser = userCaptor.getValue(); // 낚아챈 객체를 꺼내옴
        assertThat(savedUser.getEmail()).isEqualTo(command.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encoded_password_value");
        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(savedUser.getName()).isEqualTo(command.getName());
        assertThat(savedUser.getPhone()).isEqualTo(command.getPhone());
    }

    @Test
    @DisplayName("이미 가입된 이메일로 명령 시 예외가 발생해야 한다")
    void should_ThrowBusinessException_When_EmailAlreadyExists() {
        // given
        SignupCommand command = SignupCommand.builder()
                .email("duplicate@teno.com")
                .password("Password123")
                .name("테스터")
                .phone("010-1234-5678")
                .termIds(List.of(1L))
                .build();

        given(userRepository.existsByEmail(command.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> signupService.signup(command))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(GlobalErrorCode.USER_ALREADY_EXISTS);
    }
}
