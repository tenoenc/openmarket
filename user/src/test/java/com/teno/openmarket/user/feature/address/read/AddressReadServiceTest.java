package com.teno.openmarket.user.feature.address.read;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AddressReadServiceTest {

    @InjectMocks
    private AddressReadService addressReadService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private AddressReadMapper addressReadMapper;

    @Test
    @DisplayName("목록 조회 시 Repository에서 반환한 순서(기본 배송지 우선, 최신순)대로 DTO 목록을 반환해야 한다")
    void should_ReturnAddressList_InOrder() {
        // given
        Long userId = 1L;
        UserAddress defaultAddress = UserAddress.builder().isDefault(true).recipientName("기본").build();
        UserAddress normalAddress = UserAddress.builder().isDefault(false).recipientName("일반").build();

        AddressInfoResponse defaultResponse = AddressInfoResponse.builder().isDefault(true).recipientName("기본").build();
        AddressInfoResponse normalResponse = AddressInfoResponse.builder().isDefault(false).recipientName("일반").build();

        given(userAddressRepository.findAllByUserIdOrderByIsDefaultDescIdDesc(userId))
                .willReturn(List.of(defaultAddress, normalAddress));

        given(addressReadMapper.toResponse(defaultAddress)).willReturn(defaultResponse);
        given(addressReadMapper.toResponse(normalAddress)).willReturn(normalResponse);

        // when
        List<AddressInfoResponse> responses = addressReadService.getAddresses(userId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.getFirst().isDefault()).isTrue();
        assertThat(responses.getFirst().getRecipientName()).isEqualTo("기본");
    }

    @Test
    @DisplayName("상세 조회 시 본인의 배송지 ID이면 정상적으로 정보를 반환해야 한다")
    void should_ReturnAddressDetail_When_OwnAddressIdProvided() {
        // given
        Long userId = 1L;
        Long addressId = 100L;
        UserAddress address = UserAddress.builder().recipientName("홍길동").build();
        AddressInfoResponse responseDto = AddressInfoResponse.builder().recipientName("홍길동").build();

        given(userAddressRepository.findByIdAndUserId(addressId, userId)).willReturn(Optional.of(address));
        given(addressReadMapper.toResponse(address)).willReturn(responseDto);

        // when
        AddressInfoResponse response = addressReadService.getAddress(addressId, userId);

        // then
        assertThat(response.getRecipientName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("다른 사용자의 배송지 ID를 조회하려고 하면 예외(NOT_FOUND)가 발생해야 한다")
    void should_ThrowException_When_OtherUsersAddressIdProvided() {
        // given
        Long userId = 1L;
        Long addressId = 100L;

        // 내 소유가 아니므로 Optional.empty() 반환
        given(userAddressRepository.findByIdAndUserId(addressId, userId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> addressReadService.getAddress(addressId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(UserErrorCode.USER_ADDRESS_NOT_FOUND.getMessage());
    }
}
