package com.teno.openmarket.user.feature.address.update;

import com.teno.openmarket.user.feature.UserRoleControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AddressUpdateApi.class)
public class AddressUpdateApiTest extends UserRoleControllerTest {

    @MockitoBean
    private AddressUpdateService addressUpdateService;

    @MockitoBean
    private AddressUpdateMapper addressUpdateMapper;

    @Test
    @DisplayName("유효한 요청으로 배송지 수정 API 호출 시 200 OK를 반환해야 한다")
    void should_ReturnSuccess_When_UpdateAddressApiCalled() throws Exception {
        // given
        Long addressId = 10L;
        AddressUpdateRequest request = AddressUpdateRequest.builder()
                .addressName("회사")
                .recipientName("이순신")
                .recipientPhone("010-9999-8888")
                .zipCode("54321")
                .addressBase("서울시 중구")
                .addressDetail("101호")
                .build();

        AddressUpdateCommand command = AddressUpdateCommand.builder().recipientName("이순신").build();
        given(addressUpdateMapper.toCommand(any(AddressUpdateRequest.class))).willReturn(command);

        // when & then
        mockMvc.perform(put("/api/v1/users/addresses/{addressId}", addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        verify(addressUpdateService).updateAddress(eq(addressId), eq(1L), any(AddressUpdateCommand.class));

    }
}
