package com.teno.openmarket.user.feature.signup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SignupMapperTest {

    private final SignupMapper signupMapper = Mappers.getMapper(SignupMapper.class);

    @Test
    @DisplayName("SignupRequest가 UserCreateCommand로 올바르게 변환되어야 한다")
    void should_MapToUserCreateCommand_When_TransferringRequest() {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .password("password1234")
                .name("홍길동")
                .phone("010-1234-5678")
                .termIds(List.of(1L, 2L, 3L))
                .build();

        // when
        SignupCommand command = signupMapper.toCommand(request);

        // then
        assertThat(command).isNotNull();
        assertThat(command.getEmail()).isEqualTo(request.getEmail());
        assertThat(command.getPassword()).isEqualTo(request.getPassword());
        assertThat(command.getName()).isEqualTo(request.getName());
        assertThat(command.getPhone()).isEqualTo(request.getPhone());
        assertThat(command.getTermIds()).isEqualTo(request.getTermIds());
    }
}