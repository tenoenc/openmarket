package com.teno.openmarket.user.feature.profile.update;

import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.test.support.WithMockUserId;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({ProfileUpdateApi.class, TestUserSecurityConfig.class})
public class ProfileUpdateApiTest extends BaseControllerTest {

    @MockitoBean
    private ProfileUpdateService profileUpdateService;

    @MockitoBean
    private ProfileUpdateMapper profileUpdateMapper;

    @Test
    @DisplayName("유효한 수정 요청 시 프로필을 업데이트하고 결과를 반환해야 한다")
    @WithMockUserId(1L)
    void should_UpdateProfile_When_RequestIsValid() throws Exception {
        // given
        ProfileUpdateRequest request = ProfileUpdateRequest.builder()
                .name("new_name")
                .phone("010-1234-5678")
                .build();

        ProfileUpdateCommand command = ProfileUpdateCommand.builder()
                .name("new_name")
                .phone("010-1234-5678")
                .build();

        ProfileUpdateResponse response = ProfileUpdateResponse.builder()
                .id(1L)
                .email("user@test.com")
                .name("new_name")
                .phone("010-1234-5678")
                .build();

        given(profileUpdateMapper.toCommand(any(ProfileUpdateRequest.class))).willReturn(command);
        given(profileUpdateService.updateProfile(eq(1L), any(ProfileUpdateCommand.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("new_name"))
                .andExpect(jsonPath("$.data.phone").value("010-1234-5678"));
    }
}
