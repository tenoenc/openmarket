package com.teno.openmarket.user.feature.address.create;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.address.UserAddress;
import com.teno.openmarket.user.domain.address.UserAddressRepository;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressCreateService {

    private static final int MAX_ADDRESS_LIMIT = 5;
    private final UserAddressRepository userAddressRepository;

    /**
     * 배송지 등록
     * <p>
     * 사용자의 배송지를 등록합니다. 무분별한 등록을 막기 위해 최대 5개까지만 등록 가능합니다.
     * 등록된 배송지가 없을 경우, 자동으로 기본 배송지({@code isDefault=true})로 설정됩니다.
     *
     * @param userId 배송지를 등록할 사용자의 고유 식별자
     * @param command 등록할 배송지 정보
     * @throws BusinessException 등록된 배송지가 이미 5개 이상일 경우 ({@link UserErrorCode#USER_ADDRESS_LIMIT})
     */
    @Transactional
    public void createAddress(Long userId, AddressCreateCommand command) {
        long currentCount = userAddressRepository.countByUserId(userId);

        if (currentCount >= MAX_ADDRESS_LIMIT) {
            throw new BusinessException(UserErrorCode.USER_ADDRESS_LIMIT);
        }

        // 기존 배송지가 0개면 무조건 기본 배송지로 설정
        boolean isDefault = (currentCount == 0);

        UserAddress userAddress = UserAddress.builder()
                .userId(userId)
                .addressName(command.getAddressName())
                .recipientName(command.getRecipientName())
                .recipientPhone(command.getRecipientPhone())
                .zipCode(command.getZipCode())
                .addressBase(command.getAddressBase())
                .addressDetail(command.getAddressDetail())
                .isDefault(isDefault)
                .build();

        userAddressRepository.save(userAddress);
    }
}
