package com.teno.openmarket.user.feature.profile.info;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProfileInfoServiceTest {

    @InjectMocks
    private ProfileInfoService profileInfoService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유효한 유저 ID로 조회 시 프로필 정보를 반환해야 한다")
    void should_ReturnProfileInfo_When_ValidUserIdProvided() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("user@test.com")
                .name("tester")
                .phone("010-1234-5678")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        ProfileInfoResponse response = profileInfoService.getProfile(userId);

        // then
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getEmail()).isEqualTo("user@test.com");
        assertThat(response.getName()).isEqualTo("tester");
        assertThat(response.getPhone()).isEqualTo("010-1234-5678");
    }

    @Test
    @DisplayName("존재하지 않는 유저 ID로 조회 시 예외가 발생해야 한다")
    void should_ThrowException_When_UserNotFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> profileInfoService.getProfile(userId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrorCode.USER_NOT_FOUND);
    }
}
