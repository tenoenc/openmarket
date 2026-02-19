package com.teno.openmarket.user.feature.profile.update;

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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProfileUpdateServiceTest {

    @InjectMocks
    private ProfileUpdateService profileUpdateService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유효한 변경 데이터가 주어지면 엔티티를 수정하고 응답을 반환해야 한다")
    void should_UpdateProfile_When_ValidRequestProvided() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("user@test.com")
                .name("old_name")
                .phone("010-1111-2222")
                .build();

        ProfileUpdateCommand command = ProfileUpdateCommand.builder()
                .name("new_name")
                .phone("010-3333-4444")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        ProfileUpdateResponse response = profileUpdateService.updateProfile(userId, command);

        // then
        assertThat(user.getName()).isEqualTo("new_name");
        assertThat(user.getPhone()).isEqualTo("010-3333-4444");

        assertThat(response.getName()).isEqualTo("new_name");
        assertThat(response.getPhone()).isEqualTo("010-3333-4444");
    }

}
