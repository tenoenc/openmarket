package com.teno.openmarket.shop.feature.approval;

import com.teno.openmarket.shop.feature.ShopControllerTest;
import com.teno.openmarket.test.support.WithMockAdminId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ShopApprovalApi.class)
public class ShopApprovalApiTest extends ShopControllerTest {

    @MockitoBean
    private ShopApprovalService shopApprovalService;

    @Test
    @WithMockAdminId
    @DisplayName("관리자(ADMIN)는 상점 입점을 승인할 수 있다")
    void should_ReturnOk_When_AdminApprovesShop() throws Exception {
        // given
        Long shopId = 1L;
        ShopApprovalRequest request = ShopApprovalRequest.builder()
                .decision(ApprovalDecision.APPROVE)
                .build();

        // when & then
        mockMvc.perform(patch("/api/v1/admin/shops/{shopId}/approval", shopId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(shopApprovalService).processApproval(eq(shopId), any(ShopApprovalCommand.class));
    }
}
