package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.test.support.WithMockUserId;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({AddressDeleteApi.class, TestUserSecurityConfig.class})
public class AddressDeleteApiTest extends BaseControllerTest {

    @MockitoBean
    private AddressDeleteService addressDeleteService;

    @Test
    @WithMockUserId(1L)
    @DisplayName("배송지 삭제 API 호출 시 200 OK를 반환해야 한다")
    void should_ReturnSuccess_When_DeleteAddressApiCalled() throws Exception {
        // given
        Long addressId = 10L;

        // when & then
        mockMvc.perform(delete("/api/v1/users/addresses/{addressId}", addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(addressDeleteService).deleteAddress(addressId, 1L);
    }
}
