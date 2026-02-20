package com.teno.openmarket.user.feature.reissue;

import com.teno.openmarket.user.feature.UserControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
@Import(ReissueApi.class)
class ReissueApiTest extends UserControllerTest {

    @MockitoBean
    private ReissueService reissueService;

    @MockitoBean
    private ReissueMapper reissueMapper;

    @Test
    @DisplayName("유효한 Refresh Token으로 재발급 요청 시 200 OK와 새 토큰을 반환한다")
    void should_ReturnOkAndNewToken_When_RefreshTokenIsValid() throws Exception {
        // given
        ReissueRequest request = new ReissueRequest("valid_refresh_token");
        ReissueCommand command = new ReissueCommand("valid_refresh_token");

        // Reissue 패키지 내의 TokenResponse 사용
        TokenResponse response = TokenResponse.builder()
                .accessToken("new_access_token")
                .refreshToken("new_refresh_token")
                .expiresIn(1800000L)
                .build();

        given(reissueMapper.toCommand(any(ReissueRequest.class))).willReturn(command);
        given(reissueService.reissue(any(ReissueCommand.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS")) // ApiResponse 구조 검증
                .andExpect(jsonPath("$.data.accessToken").value("new_access_token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new_refresh_token"));
    }
}