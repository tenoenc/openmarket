package com.teno.openmarket.user.feature.profile.info;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileInfoService {

    private final UserRepository userRepository;

    /**
     * 로그인된 사용자의 프로필 단건 조회 처리
     * <p>
     * 인증된 사용자 ID(userId)를 기반으로 DB에서 사용자 엔티티를 조회하여 DTO로 변환합니다.
     *
     * @param userId 조회할 사용자의 고유 식별자 (PK)
     * @return 비밀번호가 제외된 프로필 응답 객체
     * @throws BusinessException 유저를 찾지 못할 경우 ({@link GlobalErrorCode#USER_NOT_FOUND})
     */
    public ProfileInfoResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));

        return ProfileInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}
