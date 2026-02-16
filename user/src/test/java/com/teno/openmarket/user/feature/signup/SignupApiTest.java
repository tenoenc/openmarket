package com.teno.openmarket.user.feature.signup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({SignupApi.class, TestUserSecurityConfig.class})
class SignupApiTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SignupService signupService;

    @MockitoBean
    private SignupMapper signupMapper;

    @Test
    @DisplayName("회원가입 요청 시 정상적으로 성공 응답을 반환해야 한다")
    void should_ReturnResponseSuccessfully_When_RequestingSignup() throws Exception {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("test@teno.com")
                .password("Password123!")
                .name("테스터")
                .phone("010-1234-5678")
                .termIds(List.of(1L, 2L))
                .build();

        SignupCommand command = SignupCommand.builder()
                .email("test@teno.com")
                .build();

        given(signupMapper.toCommand(any(SignupRequest.class)))
                .willReturn(command);

        given(signupService.signup(any(SignupCommand.class)))
                .willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(signupMapper).toCommand(any(SignupRequest.class));
        verify(signupService).signup(any(SignupCommand.class));
    }
}