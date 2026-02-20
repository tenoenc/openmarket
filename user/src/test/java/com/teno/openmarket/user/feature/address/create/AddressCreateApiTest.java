package com.teno.openmarket.user.feature.address.create;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AddressCreateApi.class)
public class AddressCreateApiTest extends UserRoleControllerTest {

    @MockitoBean
    private AddressCreateService addressCreateService;

    @MockitoBean
    private AddressCreateMapper addressCreateMapper;

    @Test
    @DisplayName("유효한 배송지 정보가 주어지면 배송지를 등록하고 200 OK를 반환해야 한다")
    void should_ReturnSuccess_When_ValidAddressRequest() throws Exception {
        // given
        AddressCreateRequest request = AddressCreateRequest.builder()
                .addressName("우리집")
                .recipientName("홍길동")
                .recipientPhone("010-1234-5678")
                .zipCode("12345")
                .addressBase("서울시 강남구 테헤란로")
                .addressDetail("101동 202호")
                .build();

        AddressCreateCommand command = AddressCreateCommand.builder()
                .addressName("우리집")
                .recipientName("홍길동")
                .recipientPhone("010-1234-5678")
                .zipCode("12345")
                .addressBase("서울시 강남구 테헤란로")
                .addressDetail("101동 202호")
                .build();

        given(addressCreateMapper.toCommand(any(AddressCreateRequest.class))).willReturn(command);

        // when & then
        mockMvc.perform(post("/api/v1/users/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"));

        // Service 계층으로 매핑된 Command와 userId(1L)가 잘 넘어갔는지 검증
        verify(addressCreateService).createAddress(eq(1L), any(AddressCreateCommand.class));
    }
}
