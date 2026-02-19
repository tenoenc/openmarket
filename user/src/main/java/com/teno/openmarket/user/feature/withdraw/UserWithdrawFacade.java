package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.user.feature.logout.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * [회원 탈퇴 파사드]
 * <p>
 * 기능 간의 결합도를 낮추고 유스케이스의 원자성(Transaction)을 보장합니다.
 */
@Component
@RequiredArgsConstructor
public class UserWithdrawFacade {
    private final UserWithdrawService userWithdrawService;
    private final LogoutService logoutService;

    /**
     * 회원 탈퇴 유스케이스
     * <p>로그아웃(Redis) 처리 중 실패할 경우, 트랜잭션 롤백에 의해 유저 탈퇴(MySQL) 내역도 취소됩니다.</p>
     * <ol>
     * <li>DB에서 유저 상태를 탈퇴로 변경 (MySQL)</li>
     * <li>로그인된 토큰을 블랙리스트 처리 (Redis)</li>
     * </ol>
     */
    @Transactional
    public void withdrawAndLogout(Long userId, String accessToken) {
        userWithdrawService.withdraw(userId);
        logoutService.logout(accessToken);
    }
}
