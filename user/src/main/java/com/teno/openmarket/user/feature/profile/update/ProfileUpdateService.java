package com.teno.openmarket.user.feature.profile.update;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileUpdateService {

    private final UserRepository userRepository;

    /**
     * 사용자의 이름과 전화번호 정보를 업데이트합니다.
     *
     * @param userId  수정할 사용자의 고유 식별자 (PK)
     * @param command 수정될 프로필 정보(이름, 전화번호)를 담은 객체
     * @return {@link ProfileUpdateResponse} 수정이 완료된 후의 사용자 정보
     * @throws BusinessException 유저를 찾지 못할 경우 ({@link GlobalErrorCode#USER_NOT_FOUND})
     */
    @Transactional
    public ProfileUpdateResponse updateProfile(Long userId, ProfileUpdateCommand command) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_NOT_FOUND));

        user.updateProfile(command.getName(), command.getPhone());

        return ProfileUpdateResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
}
