package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddressDeleteServiceTest {

    @InjectMocks
    private AddressDeleteService addressDeleteService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Test
    @DisplayName("배송지 삭제 시 기본 배송지라면 예외가 발생해야 한다")
    void should_ThrowException_When_DeletingDefaultAddress() {
        // given
        Long userId = 1L;
        Long addressId = 10L;
        UserAddress defaultAddress = UserAddress.builder().isDefault(true).build();

        given(userAddressRepository.findByIdAndUserId(addressId, userId)).willReturn(Optional.of(defaultAddress));

        // when & then
        assertThatThrownBy(() -> addressDeleteService.deleteAddress(addressId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(GlobalErrorCode.USER_ADDRESS_DEFAULT.getMessage());
    }

    @Test
    @DisplayName("일반 배송지 삭제 시 정상적으로 삭제되어야 한다")
    void should_DeleteAddress_When_NotDefaultAddress() {
        // given
        Long userId = 1L;
        Long addressId = 10L;
        UserAddress normalAddress = UserAddress.builder().isDefault(false).build();

        given(userAddressRepository.findByIdAndUserId(addressId, userId)).willReturn(Optional.of(normalAddress));

        // when
        addressDeleteService.deleteAddress(addressId, userId);

        // then
        verify(userAddressRepository).delete(normalAddress);
    }
}
