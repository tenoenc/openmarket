package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
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

    public List<AddressInfoResponse> getAddresses(Long userId) {
        return userAddressRepository.findAllByUserIdOrderByIsDefaultDescIdDesc(userId)
                .stream()
                .map(addressReadMapper::toResponse)
                .toList();
    }

    public AddressInfoResponse getAddress(Long addressId, Long userId) {
        UserAddress address = userAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new BusinessException(GlobalErrorCode.USER_ADDRESS_NOT_FOUND));

        return addressReadMapper.toResponse(address);
    }
}
