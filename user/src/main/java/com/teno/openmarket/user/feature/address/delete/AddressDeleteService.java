package com.teno.openmarket.user.feature.address.delete;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressDeleteService {

    private final UserAddressRepository userAddressRepository;

    /**
     * 배송지 삭제
     * <p>
     * 특정 배송지를 삭제합니다.
     * 단, 기본 배송지(isDefault=true)로 설정된 항목은 삭제할 수 없으며 예외가 발생합니다.
     *
     * @param addressId 삭제할 배송지의 고유 식별자
     * @param userId 삭제를 요청한 사용자의 고유 식별자 (소유권 검증용)
     * @throws BusinessException 본인의 배송지가 아니거나 존재하지 않을 경우 ({@link GlobalErrorCode#USER_ADDRESS_NOT_FOUND})
     * @throws BusinessException 기본 배송지를 삭제하려고 시도할 경우 ({@link GlobalErrorCode#USER_ADDRESS_DEFAULT})
     */
    @Transactional
    public void deleteAddress(Long addressId, Long userId) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_ADDRESS_NOT_FOUND));

        if (address.getIsDefault()) {
            throw new BusinessException(GlobalErrorCode.USER_ADDRESS_DEFAULT);
        }

        userAddressRepository.delete(address);
    }
}
