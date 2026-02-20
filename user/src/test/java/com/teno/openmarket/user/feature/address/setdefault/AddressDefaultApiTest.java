package com.teno.openmarket.user.feature.address.setdefault;

import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.test.support.WithMockUserId;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({AddressDefaultApi.class, TestUserSecurityConfig.class})
public class AddressDefaultApiTest extends BaseControllerTest {

    @MockitoBean
    private AddressDefaultService addressDefaultService;

    @Test
    @WithMockUserId(1L)
    @DisplayName("기본 배송지 설정 API 호출 시 성공 응답을 반화해야 한다")
    void should_ReturnSuccess_When_PatchDefaultAddressApiCalled() throws Exception {
        // given
        Long addressId = 10L;

        // when & then
        mockMvc.perform(patch("/api/v1/users/addresses/{addressId}/default", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(addressDefaultService).setDefaultAddress(addressId, 1L);
    }
}
