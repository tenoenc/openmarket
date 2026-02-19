package com.teno.openmarket.user.feature.address.create;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AddressCreateServiceTest {

    @InjectMocks
    private AddressCreateService addressCreateService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Test
    @DisplayName("아무것도 없는 상태에서 첫 배송지를 등록하면 isDefault가 true로 저장되어야 한다")
    void should_SetAsDefault_When_FirstAddress() {
        // given
        Long userId = 1L;
        AddressCreateCommand command = createDummyCommand();

        given(userAddressRepository.countByUserId(userId)).willReturn(0L);

        // when
        addressCreateService.createAddress(userId, command);

        // then
        ArgumentCaptor<UserAddress> captor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(captor.capture());

        UserAddress savedAddress = captor.getValue();
        assertThat(savedAddress.getIsDefault()).isTrue();
    }

    @Test
    @DisplayName("기존 배송지가 있는 상태에서 추가 등록하면 isDefault가 false로 저장되어야 한다")
    void should_SetasNotDefault_When_SubsequentAddress() {
        // given
        Long userId = 1L;
        AddressCreateCommand command = createDummyCommand();

        given(userAddressRepository.countByUserId(userId)).willReturn(1L);

        // when
        addressCreateService.createAddress(userId, command);

        // then
        ArgumentCaptor<UserAddress> captor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(captor.capture());

        UserAddress savedAddress = captor.getValue();
        assertThat(savedAddress.getIsDefault()).isFalse();
    }

    @Test
    @DisplayName("배송지가 이미 5개인 상태에서 추가 등록 시도 시 예외가 발생해야 한다")
    void should_ThrowException_When_AddressLimitExceeded() {
        // given
        Long userId = 1L;
        AddressCreateCommand command = createDummyCommand();

        // 기존 배송지가 5개 꽉 참
        given(userAddressRepository.countByUserId(userId)).willReturn(5L);

        // when & then
        assertThatThrownBy(() -> addressCreateService.createAddress(userId, command))
                .isInstanceOf(BusinessException.class)
                .hasMessage(GlobalErrorCode.USER_ADDRESS_LIMIT.getMessage());

    }

    private AddressCreateCommand createDummyCommand() {
        return AddressCreateCommand.builder()
                .addressName("집")
                .recipientName("홍길동")
                .recipientPhone("010-1234-5678")
                .zipCode("12345")
                .addressBase("서울시 강남구")
                .addressDetail("101동")
                .build();
    }

}
