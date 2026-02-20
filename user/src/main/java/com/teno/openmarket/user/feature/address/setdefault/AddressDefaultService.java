package com.teno.openmarket.user.feature.address.setdefault;

import com.teno.openmarket.core.security.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressDefaultService {

    private final UserAddressRepository userAddressRepository;

    /**
     * 기본 배송지 변경
     * <p>
     * 기존 기본 배송지를 일반 배송지로 강등하고, 요청된 배송지를 기본 배송지로 승격합니다.
     * 이 작업은 원자적으로 처리되며, 도중 예외 발생 시 전체 롤백됩니다.
     *
     * @param addressId 기본 배송지로 설정할 대상 식별자
     * @param userId 요청한 사용자의 고유 식별자 (소유권 검증용)
     * @throws BusinessException 대상 배송지를 찾을 수 없을 때 ({@link UserErrorCode#USER_ADDRESS_NOT_FOUND})
     */
    @Transactional
    public void setDefaultAddress(Long addressId, Long userId) {
        // 1. 타겟 배송지 조회 (소유권 없으면 예외 발생하여 롤백됨)
        UserAddress targetAddress = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_ADDRESS_NOT_FOUND));

        // 이미 기본 배송지라면 불필요한 DB 쿼리를 막기 위해 바로 종료
        if (targetAddress.getIsDefault()) {
            return;
        }

        // 2. 기존 기본 배송지 조회 및 해제
        userAddressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(oldDefault -> oldDefault.changeDefaultStatus(false));

        // 3. 타겟 배송지를 기본 배송지로 설정
        targetAddress.changeDefaultStatus(true);
    }
}
