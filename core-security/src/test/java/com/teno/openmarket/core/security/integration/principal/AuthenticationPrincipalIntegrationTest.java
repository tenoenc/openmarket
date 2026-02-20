package com.teno.openmarket.core.security.integration.principal;

import com.teno.openmarket.core.security.JwtTokenProvider;
import com.teno.openmarket.core.security.integration.CoreSecurityIntegrationTest;
import com.teno.openmarket.common.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AuthenticationPrincipalIntegrationTest.TestController.class)
@AutoConfigureMockMvc
public class AuthenticationPrincipalIntegrationTest extends CoreSecurityIntegrationTest {

    @RestController
    static class TestController {
        @GetMapping("/core/security/test/principal")
        public ApiResponse<String> getPrincipal(@AuthenticationPrincipal Long userId) {
            String result = userId == null ? "null" : "ID:" + userId;
            return ApiResponse.success(result);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("유효한 JWT 토큰으로 요청 시 @AuthenticationPrincipal에 Long 타입의 userId가 바인딩되어야 한다")
    void should_BindLongUserId_When_ValidJwtTokenProvided() throws Exception {
        // given
        Long expectedUserId = 1004L;
        String validToken = jwtTokenProvider.createAccessToken(expectedUserId, "ROLE_USER");

        // when & then
        mockMvc.perform(get("/core/security/test/principal")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("ID:1004"));
    }
}
