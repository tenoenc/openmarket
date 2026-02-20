package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import com.teno.openmarket.shop.domain.shop.ShopStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopApplyService {

    private final ShopRepository shopRepository;

    /**
     * 상점 개설 (입점 신청)
     * <p>
     * 1인 1상점 정책을 검증하고, 초기 상태를 WAITING(대기)으로 설정하여 저장합니다.
     * 아직 관리자의 승인이 없었으므로 권한은 USER로 유지됩니다.
     *
     * @param userId 신청하는 사용자의 고유 식별자
     * @param command 입점 신청 상점 정보
     */
    @Transactional
    public void apply(Long userId, ShopApplyCommand command) {
        // 1. 이미 상점을 보유했거나 신청 중인지 검증
        if (shopRepository.existsByUserId(userId)) {
            throw new BusinessException(ShopErrorCode.SHOP_ALREADY_EXISTS);
        }

        // 2. 상점명 중복 검증 (DB Unique 제약조건 방어)
        if (shopRepository.existsByShopName(command.getShopName())) {
            throw new BusinessException(ShopErrorCode.SHOP_NAME_DUPLICATED);
        }

        // 3. 상태를 WAITING으로 상점 생성
        Shop shop = Shop.builder()
                .userId(userId)
                .shopName(command.getShopName())
                .registrationNumber(command.getRegistrationNumber())
                .description(command.getDescription())
                .bankName(command.getBankName())
                .accountNumber(command.getAccountNumber())
                .accountHolder(command.getAccountHolder())
                .status(ShopStatus.WAITING)
                .build();

        shopRepository.save(shop);
    }
}
