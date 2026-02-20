package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWithdrawService {

    private final UserRepository userRepository;

    /**
     * 유저 정보 삭제 (Soft Delete)
     * <p>
     * JPA 엔티티에 설정된 @SQLDelete에 의해 실제 데이터베이스의 행(row)은 물리적으로 삭제되지 않고,
     * {@code is_deleted} 플래그가 true로 변경되며 {@code deleted_at}에 현재 시간이 기록됩니다.
     *
     * @param userId 탈퇴 처리할 사용자의 고유 식별자 (PK)
     * @throws BusinessException 유저를 찾지 못할 경우 ({@link UserErrorCode#USER_NOT_FOUND})
     */
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}
