package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserWithdrawServiceTest {

    @InjectMocks
    private UserWithdrawService userWithdrawService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유효한 유저 ID가 주어지면 DB에서 삭제 처리를 해야 한다")
    void should_DeleteUser_When_ValidUserIdProvided() {
        // given
        Long userId = 1L;
        User user = User.builder().id(userId).email("user@test.com").build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        userWithdrawService.withdraw(userId);

        // then
        verify(userRepository).delete(user);
    }
}
