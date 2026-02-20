package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.shop.feature.ShopUserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ShopApplyApi.class)
public class ShopApplyApiTest extends ShopUserRoleControllerTest {

    @MockitoBean
    private ShopApplyService shopApplyService;

    @MockitoBean
    private ShopApplyMapper shopApplyMapper;

    @Test
    @DisplayName("유효한 요청으로 입점 신청 API 호출 시 200 OK를 반환해야 한다")
    void should_ReturnSuccess_When_ShopApplyApiCalled() throws Exception {
        // given
        ShopApplyRequest request = ShopApplyRequest.builder()
                .shopName("오픈마켓")
                .registrationNumber("123-45-67890")
                .description("질 좋은 상품만 판매합니다.")
                .bankName("한국은행")
                .accountNumber("110-123-456")
                .accountHolder("홍길동")
                .build();

        ShopApplyCommand command = ShopApplyCommand.builder()
                .shopName("오픈마켓")
                .build();

        given(shopApplyMapper.toCommand(any(ShopApplyRequest.class))).willReturn(command);

        // when & then
        mockMvc.perform(post("/api/v1/shops/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(shopApplyService).apply(eq(1L), any(ShopApplyCommand.class));
    }
}
