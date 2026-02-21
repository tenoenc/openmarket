package com.teno.openmarket.shop.feature.approval;

import com.teno.openmarket.common.event.DomainEvent;
import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import com.teno.openmarket.shop.domain.shop.ShopStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ShopApprovalServiceTest {

    @InjectMocks
    private ShopApprovalService shopApprovalService;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("상점 승인 시 상태가 ACTIVE로 변경되고, 권한 승격을 위한 DomainEvent가 발행되어야 한다")
    void should_ChangeStatusToActiveAndPublishEvent_When_Approved() {
        // given
        Long shopId = 1L;
        Long userId = 100L;
        Shop shop = Shop.builder().userId(userId).status(ShopStatus.WAITING).build();
        ShopApprovalCommand command = ShopApprovalCommand.builder().decision(ApprovalDecision.APPROVE).build();

        given(shopRepository.findById(shopId)).willReturn(Optional.of(shop));

        // when
        shopApprovalService.processApproval(shopId, command);

        // then
        assertThat(shop.getStatus()).isEqualTo(ShopStatus.ACTIVE);
        verify(eventPublisher).publishEvent(argThat((Object event) -> {
            if (event instanceof DomainEvent(String eventType, Map<String, Object> payload)) {
                return eventType.equals("SHOP_APPROVED") && payload.get("userId").equals(userId);
            }
            return false;
        }));
    }

    @Test
    @DisplayName("상점 반려 시 상태가 REJECTED로 변경되고 사유가 저장되어야 한다")
    void should_ChangeStatusToRejectedAndSaveReason_When_Rejected() {
        // given
        Long shopId = 1L;
        Shop shop = Shop.builder().status(ShopStatus.WAITING).build();
        ShopApprovalCommand command = ShopApprovalCommand.builder()
                .decision(ApprovalDecision.REJECT)
                .rejectReason("서류 마비")
                .build();

        given(shopRepository.findById(shopId)).willReturn(Optional.of(shop));

        // when
        shopApprovalService.processApproval(shopId, command);
        
        // then
        assertThat(shop.getStatus()).isEqualTo(ShopStatus.REJECTED);
        assertThat(shop.getRejectReason()).isEqualTo("서류 마비");
    }
}
