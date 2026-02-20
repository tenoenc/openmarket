package com.teno.openmarket.user.feature.address.update;

import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AddressUpdateServiceTest {

    @InjectMocks
    private AddressUpdateService addressUpdateService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Test
    @DisplayName("유효한 수정 요청 시 배송지 정보가 변경되어야 한다")
    void should_UpdateAddress_When_ValidCommandProvided() {
        // given
        Long userId = 1L;
        Long addressId = 10L;
        UserAddress address = UserAddress.builder().recipientName("기존이름").build();
        AddressUpdateCommand command = AddressUpdateCommand.builder().recipientName("새이름").build();

        given(userAddressRepository.findByIdAndUserId(addressId, userId)).willReturn(Optional.of(address));

        // when
        addressUpdateService.updateAddress(addressId, userId, command);

        // then
        assertThat(address.getRecipientName()).isEqualTo("새이름");
    }
}
