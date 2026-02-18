package com.teno.openmarket.user.feature.logout;

import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import com.teno.openmarket.common.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({LogoutApi.class, TestUserSecurityConfig.class})
public class LogoutApiTest extends BaseControllerTest {

    @MockitoBean
    private LogoutService logoutService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그아웃 요청 시 Authorization 헤더의 토큰을 서비스에 전달하고 200 OK를 반환하다")
    void should_PassThroughTokenAndReturnSuccess_When_Logout() throws Exception {
        // given
        String token = "valid-access-token";

        // when & then
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        // 서비스의 logout 메서드가 토큰값으로 호출되었는지 검증
        verify(logoutService).logout(token);
    }
}
