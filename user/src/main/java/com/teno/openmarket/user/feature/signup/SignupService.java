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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupService {

    private final UserRepository userRepository;
    private final TermRepository termRepository;
    private final TermAgreementRepository termAgreementRepository;
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
            throw new BusinessException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        // 2. 약관 검증 (필수 약관 동의 여부 체크)
        verifyMandatoryTerms(command.getTermIds());

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(command.getPassword());

        // 4. User 엔티티 생성
        User user = User.builder()
                .email(command.getEmail())
                .password(encodedPassword)
                .name(command.getName())
                .phone(command.getPhone())
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        // 5. 약관 동의 이력 저장
        saveTermAgreements(savedUser, command.getTermIds());

        return savedUser.getId();
    }

    private void verifyMandatoryTerms(List<Long> agreedTermIds) {
        // 입력된 약관 목록 자체가 비어있는 경우
        if (agreedTermIds == null || agreedTermIds.isEmpty()) {
            // 시스템에 필수 약관이 존재하는데 동의를 안 했다면
            if (!termRepository.findAllByIsRequiredTrue().isEmpty()) {
                throw new BusinessException(UserErrorCode.USER_TERMS_REQUIRED);
            }
            return;
        }

        List<Term> mandatoryTerms = termRepository.findAllByIsRequiredTrue();
        Set<Long> agreedTermIdSet = Set.copyOf(agreedTermIds);

        // 필수 약관 중 하나라도 누락된 경우
        boolean isAllMandatoryAgreed = mandatoryTerms.stream()
                .allMatch(term -> agreedTermIdSet.contains(term.getId()));

        if (!isAllMandatoryAgreed) {
            throw new BusinessException(UserErrorCode.USER_TERMS_REQUIRED);
        }
    }

    private void saveTermAgreements(User user, List<Long> termIds) {
        if (termIds == null || termIds.isEmpty()) {
            return;
        }

        List<TermAgreement> agreements = termIds.stream()
                .map(termId -> TermAgreement.builder()
                        .user(user)
                        .termId(termId)
                        .build())
                .collect(Collectors.toList());

        termAgreementRepository.saveAll(agreements);
    }
}
