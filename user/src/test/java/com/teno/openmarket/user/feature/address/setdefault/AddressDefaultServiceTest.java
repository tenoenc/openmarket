package com.teno.openmarket.user.feature.address.setdefault;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
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

@ExtendWith(MockitoExtension.class)
public class AddressDefaultServiceTest {

    @InjectMocks
    private AddressDefaultService addressDefaultService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Test
    @DisplayName("기본 배송지로 변경 시, 기존 기본 배송지는 일반으로 변경되고 타겟은 기본으로 변경되어야 한다")
    void should_SwapDefaultAddress_When_Successfully() {
        // given
        Long userId = 1L;
        Long targetAddressId = 20L;

        UserAddress oldDefault = UserAddress.builder().isDefault(true).build();
        UserAddress newDefault = UserAddress.builder().isDefault(false).build();

        given(userAddressRepository.findByIdAndUserId(targetAddressId, userId)).willReturn(Optional.of(newDefault));
        given(userAddressRepository.findByUserIdAndIsDefaultTrue(userId)).willReturn(Optional.of(oldDefault));

        // when
        addressDefaultService.setDefaultAddress(targetAddressId, userId);

        // then
        assertThat(oldDefault.getIsDefault()).isFalse();
        assertThat(newDefault.getIsDefault()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않거나 권한이 없는 배송지를 기본으로 설정하려 하면 예외가 발생해야 한다")
    void should_ThrowException_When_TargetAddressNotFound() {
        // given
        Long userId = 1L;
        Long targetAddressId = 999L;

        given(userAddressRepository.findByIdAndUserId(targetAddressId, userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addressDefaultService.setDefaultAddress(targetAddressId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(UserErrorCode.USER_ADDRESS_NOT_FOUND.getMessage());
    }
}
