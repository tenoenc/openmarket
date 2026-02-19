package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.user.feature.logout.LogoutService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
public class UserWithdrawFacadeTest {

    @InjectMocks
    private UserWithdrawFacade userWithdrawFacade;

    @Mock
    private UserWithdrawService userWithdrawService;

    @Mock
    private LogoutService logoutService;

    @Test
    @DisplayName("탈퇴 유스케이스 실행 시 삭제(A) 후 로그아웃(B) 순서로 정상 호출되어야 한다")
    void should_CallServicesInOrder_When_WithdrawAndLogout() {
        // given
        Long userId = 1L;
        String accessToken = "valid_access_token";

        // when
        userWithdrawFacade.withdrawAndLogout(userId, accessToken);

        // then
        // DB 반영 후 Redis 작업을 해야 롤백이 보장됩니다. (순서 검증 중요)
        InOrder inOrder = inOrder(userWithdrawService, logoutService);
        inOrder.verify(userWithdrawService).withdraw(userId);
        inOrder.verify(logoutService).logout(accessToken);
    }
}
