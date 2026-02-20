package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressReadService {

    private final UserAddressRepository userAddressRepository;
    private final AddressReadMapper addressReadMapper;

    /**
     * 내 배송지 목록 조회
     * <p>
     * 특정 사용자가 등록한 모든 배송지 목록을 조회합니다.
     * 기본 배송지(isDefault=true)가 최상단에 오며, 나머지는 등록 역순(id 내림차순)으로 정렬됩니다.
     *
     * @param userId 조회를 요청한 사용자의 고유 식별자
     * @return 정렬된 배송지 정보 응답 DTO 목록
     */
    public List<AddressInfoResponse> getAddresses(Long userId) {
        return userAddressRepository.findAllByUserIdOrderByIsDefaultDescIdDesc(userId)
                .stream()
                .map(addressReadMapper::toResponse)
                .toList();
    }

    /**
     * 내 배송지 상세 조회
     * <p>
     * 특정 배송지의 상세 정보를 반환합니다.
     * 보안을 위해 요청한 사용자(userId)가 해당 배송지의 소유자인지 함께 검증합니다.
     *
     * @param addressId 조회할 배송지의 고유 식별자
     * @param userId 조회를 요청한 사용자의 고유 식별자 (소유권 검증용)
     * @return 배송지 상세 정보 응답 DTO
     * @throws BusinessException 본인의 배송지가 아니거나 존재하지 않을 경우 ({@link UserErrorCode#USER_ADDRESS_NOT_FOUND})
     */
    public AddressInfoResponse getAddress(Long addressId, Long userId) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_ADDRESS_NOT_FOUND));

        return addressReadMapper.toResponse(address);
    }
}
