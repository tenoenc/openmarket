package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.test.support.WithMockUserId;
import com.teno.openmarket.user.feature.UserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AddressReadApi.class)
public class AddressReadApiTest extends UserRoleControllerTest {

    @MockitoBean
    private AddressReadService addressReadService;

    @Test
    @DisplayName("배송지 목록 조회 API 호출 시 목록을 반환해야 한다")
    void should_ReturnAddressList_When_GetAddressedApiCalled() throws Exception {
        AddressInfoResponse response = AddressInfoResponse.builder()
                .addressName("집")
                .isDefault(true)
                .build();
        given(addressReadService.getAddresses(1L)).willReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/api/v1/users/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].addressName").value("집"))
                .andExpect(jsonPath("$.data.items[0].default").value(true));
    }

    @Test
    @WithMockUserId(1L)
    @DisplayName("배송지 상세 조회 API 호출 시 상세 정보를 반환해야 한다")
    void should_ReturnAddressDetail_When_GetAddressApiCalled() throws Exception {
        // given
        AddressInfoResponse response = AddressInfoResponse.builder()
                .id(100L)
                .recipientName("홍길동")
                .build();
        given(addressReadService.getAddress(100L, 1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/users/addresses/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipientName").value("홍길동"));
    }
}
