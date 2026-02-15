package com.teno.openmarket.user.infra.security.config;

import com.teno.openmarket.user.infra.security.entrypoint.JwtAuthenticationEntryPoint;
import com.teno.openmarket.user.infra.security.filter.JwtAuthenticationFilter;
import com.teno.openmarket.user.infra.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({SecurityConfig.class, JwtAuthenticationEntryPoint.class, JwtAuthenticationFilter.class})
public class SecurityConfigTest {

    @SpringBootApplication
    static class TestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("인증되지 않은 사용자는 보호된 리소스에 접근할 수 없다 (401)")
    void should_Return401_When_AccessingSecuredResource_WithoutToken() throws Exception {
        // /api/v1/users/me 는 인증이 필요하다고 가정
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isUnauthorized());
        // if 403 -> 인증 O, 권한 X
        // if 200 -> 보안 설정 뚫림
    }

    @Test
    @DisplayName("인증이 필요 없는 허용된 URL은 통과해야 한다 (404/200)")
    void should_AllowAccess_To_PublicEndpoints() throws Exception {
        // /api/v1/auth/login 은 permitAll 이어야 함
        mockMvc.perform(get("/api/v1/auth/login"))
                .andExpect(status().is(404)); // 401이 아니어야 함
    }
}
