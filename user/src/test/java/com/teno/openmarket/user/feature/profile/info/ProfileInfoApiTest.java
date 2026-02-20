package com.teno.openmarket.user.feature.profile.info;

import com.teno.openmarket.user.feature.UserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ProfileInfoApi.class)
public class ProfileInfoApiTest extends UserRoleControllerTest {

    @MockitoBean
    private ProfileInfoService profileInfoService;

    @Test
    @DisplayName("내 정보 조회 API 호출 시 프로필 정보를 반환해야 한다")
    void should_ReturnProfileInfo_When_GetMyProfileApiCalled() throws Exception {
        // given
        ProfileInfoResponse response = ProfileInfoResponse.builder()
                .id(1L)
                .email("user@test.com")
                .name("tester")
                .phone("010-1234-5678")
                .build();

        given(profileInfoService.getProfile(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("user@test.com"))
                .andExpect(jsonPath("$.data.name").value("tester"));
    }
}