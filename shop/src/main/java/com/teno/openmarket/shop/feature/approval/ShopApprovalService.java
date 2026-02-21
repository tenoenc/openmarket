package com.teno.openmarket.shop.feature.approval;

import com.teno.openmarket.common.event.DomainEvent;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShopApprovalService {

    private final ShopRepository shopRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 관리자 상점 승인 및 반려 처리
     * <p>
     * 관리자의 결정에 따라 상점의 상태를 변경합니다.
     * 승인 시 권한 승격을 위한 범용 도메인 이벤트를 발행하고, 반려 시 반려 사유를 저장합니다.
     *
     * @param shopId  상태를 변경할 상점의 고유 식별자 (PK)
     * @param command 승인 또는 반려 결정과 사유가 포함된 커맨드 객체
     * @throws BusinessException 상점을 찾을 수 없는 경우 ({@link ShopErrorCode#SHOP_NOT_FOUND})
     */
    @Transactional
    public void processApproval(Long shopId, ShopApprovalCommand command) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BusinessException(ShopErrorCode.SHOP_NOT_FOUND));

        if (command.getDecision() == ApprovalDecision.APPROVE) {
            shop.approve();
            // 범용 DomainEvent 발행 (타입명과 페이로드 전달)
            eventPublisher.publishEvent(new DomainEvent(
                "SHOP_APPROVED",
                Map.of("userId", shop.getUserId())
            ));
        } else {
            shop.reject(command.getRejectReason());
        }
    }
}
