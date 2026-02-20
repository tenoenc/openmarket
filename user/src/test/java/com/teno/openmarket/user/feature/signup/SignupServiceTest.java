package com.teno.openmarket.user.feature.signup;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.term.Term;
import com.teno.openmarket.user.domain.term.TermAgreement;
import com.teno.openmarket.user.domain.term.TermAgreementRepository;
import com.teno.openmarket.user.domain.term.TermRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignupServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TermRepository termRepository;

    @Mock
    private TermAgreementRepository termAgreementRepository;

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
                .isEqualTo(UserErrorCode.USER_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("필수 약관에 동의하지 않으면 예외가 발생해야 한다")
    void should_ThrowBusinessException_When_MandatoryTermsAreNotAgreed() {
        // given
        SignupCommand command = SignupCommand.builder()
                .email("test@teno.com")
                .termIds(List.of(1L))
                .build();

        Term mandatoryTerm1 = Term.builder().id(1L).isRequired(true).build();
        Term mandatoryTerm2 = Term.builder().id(2L).isRequired(true).build();

        // 필수 약관은 2개라고 가정
        given(termRepository.findAllByIsRequiredTrue())
                .willReturn(List.of(mandatoryTerm1, mandatoryTerm2));

        // when & then
        assertThatThrownBy(() -> signupService.signup(command))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_TERMS_REQUIRED);
    }

    @Test
    @DisplayName("정상 가입 시 회원 정보와 약관 동의 이력이 모두 저장되어야 한다")
    void should_SaveUserAndAgreements_When_ValidSignupRequest() {
        // given
        SignupCommand command = SignupCommand.builder()
                .email("test@teno.com")
                .password("password")
                .name("테스터")
                .phone("010-1234-5678")
                .termIds(List.of(1L, 2L))
                .build();

        Term mandatoryTerm1 = Term.builder().id(1L).isRequired(true).build();
        Term mandatoryTerm2 = Term.builder().id(2L).isRequired(true).build();

        given(termRepository.findAllByIsRequiredTrue())
                .willReturn(List.of(mandatoryTerm1, mandatoryTerm2));

        given(userRepository.existsByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn("encoded_password");

        User savedUser = User.builder().id(100L).email("test@teno.com").build();
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        Long userId = signupService.signup(command);

        // then
        assertThat(userId).isEqualTo(100L);

        // 1. 유저 저장 검증
        verify(userRepository).save(any(User.class));

        // 2. 약관 동의 이력 저장 검증
        verify(termAgreementRepository).saveAll(argThat(agreements -> {
            List<TermAgreement> list = (List<TermAgreement>) agreements;
            return list.size() == 2
                    && list.stream().anyMatch(a -> a.getTermId() == 1L)
                    && list.stream().anyMatch(a -> a.getTermId() == 2L)
                    && list.stream().anyMatch(a -> a.getUser().getId() == 100L);
        }));
    }
}
