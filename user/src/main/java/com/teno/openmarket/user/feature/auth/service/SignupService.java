package com.teno.openmarket.user.feature.auth.service;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.entity.User;
import com.teno.openmarket.user.domain.repository.UserRepository;
import com.teno.openmarket.user.domain.vo.Role;
import com.teno.openmarket.user.feature.auth.dto.SignupCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 처리
     *
     * @param command 회원가입 명령 DTO (검증 완료된 데이터)
     * @return 저장된 User의 ID
     */
    @Transactional
    public Long signup(SignupCommand command) {
        // 1. 이메일 중복 검사
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new BusinessException(GlobalErrorCode.USER_ALREADY_EXISTS);
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        // 3. User 엔티티 생성
        User user = User.builder()
                .email(command.getEmail())
                .password(encodedPassword)
                .name(command.getName())
                .phone(command.getPhone())
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user).getId();
    }
}
