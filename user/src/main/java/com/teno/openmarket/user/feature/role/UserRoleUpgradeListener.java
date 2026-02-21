package com.teno.openmarket.user.feature.role;

import com.teno.openmarket.common.event.DomainEvent;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.user.domain.exception.UserErrorCode;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleUpgradeListener {

    private final UserRepository userRepository;

    @EventListener(condition = "#event.eventType() == 'SHOP_APPROVED'")
    public void handleShopApprovedEvent(DomainEvent event) {
        Long userId = (Long) event.payload().get("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        user.upgradeToSeller();
    }
}
