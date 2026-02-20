package com.teno.openmarket.user.feature.withdraw;

import com.teno.openmarket.user.feature.UserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(UserWithdrawApi.class)
public class UserWithdrawApiTest extends UserRoleControllerTest {

    @MockitoBean
    private UserWithdrawFacade userWithdrawFacade;

    @Test
    @DisplayName("회원 탈퇴 API 호출 시 200 OK를 반환하고 Facade 로직을 호출해야 한다")
    void should_ReturnSuccess_When_WithdrawApiCalled() throws Exception {
        // given
        String authHeader = "Bearer valid_access_token";

        // when & then
        mockMvc.perform(delete("/api/v1/users/me")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(userWithdrawFacade).withdrawAndLogout(1L, "valid_access_token");
    }
}
