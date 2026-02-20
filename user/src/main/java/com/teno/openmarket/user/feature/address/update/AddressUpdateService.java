package com.teno.openmarket.user.feature.address.update;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressUpdateService {

    private final UserAddressRepository userAddressRepository;

    /**
     * 배송지 정보 수정
     * <p>
     * 특정 배송지의 상세 정보를 업데이트합니다. 소유권 검증을 위해 {@code userId}를 함께 확인합니다.
     *
     * @param addressId 수정할 배송지의 고유 식별자
     * @param userId 수정을 요청한 사용자의 고유 식별자 (소유권 검증용)
     * @param command 수정할 배송지 정보
     * @throws BusinessException 본인의 배송지가 아니거나 존재하지 않을 경우 ({@link GlobalErrorCode#USER_ADDRESS_NOT_FOUND})
     */
    @Transactional
    public void updateAddress(Long addressId, Long userId, AddressUpdateCommand command) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_ADDRESS_NOT_FOUND));

        address.updateAddress(
                command.getAddressName(),
                command.getRecipientName(),
                command.getRecipientPhone(),
                command.getZipCode(),
                command.getAddressBase(),
                command.getAddressDetail()
        );
    }
}
