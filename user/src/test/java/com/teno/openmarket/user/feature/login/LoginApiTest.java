package com.teno.openmarket.user.feature.login;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({LoginApi.class, TestUserSecurityConfig.class})
class LoginApiTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginService loginService;

    @MockitoBean
    private LoginMapper loginMapper;

    @Test
    @DisplayName("유효한 로그인 요청 시 200 OK와 토큰을 반환한다")
    void should_ReturnOkAndToken_When_LoginRequestIsValid() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@teno.com")
                .password("Password123!")
                .build();

        LoginCommand command = LoginCommand.builder()
                .email("test@teno.com")
                .password("Password123!")
                .build();

        TokenResponse response = TokenResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .expiresIn(1800000L)
                .build();

        given(loginMapper.toCommand(any(LoginRequest.class))).willReturn(command);
        given(loginService.login(any(LoginCommand.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("access_token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh_token"))
                .andExpect(jsonPath("$.data.expiresIn").value("1800000"));
    }

    @Test
    @DisplayName("이메일 형식이 잘못되면 400 Bad Request를 반환한다")
    void should_ReturnBadRequest_When_EmailIsInvalid() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("invalid-email")
                .password("Password123!")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
