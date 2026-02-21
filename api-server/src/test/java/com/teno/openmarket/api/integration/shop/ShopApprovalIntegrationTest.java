package com.teno.openmarket.api.integration.shop;

import com.teno.openmarket.api.integration.ApiIntegrationTest;
import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import com.teno.openmarket.shop.domain.shop.ShopStatus;
import com.teno.openmarket.shop.feature.approval.ApprovalDecision;
import com.teno.openmarket.shop.feature.approval.ShopApprovalCommand;
import com.teno.openmarket.shop.feature.approval.ShopApprovalService;
import com.teno.openmarket.user.domain.user.Role;
import com.teno.openmarket.user.domain.user.User;
import com.teno.openmarket.user.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopApprovalIntegrationTest extends ApiIntegrationTest {

    @Autowired
    private ShopApprovalService shopApprovalService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("상점을 승인하면, 이벤트가 정상 발행/수신되어 유저 권한이 SELLER로 승격된다")
    void should_UpgradeUserRoleToSeller_WhenEventOccurs() {
        // given
        User user = User.builder()
                .email("user@test.com")
                .password("encoded_password")
                .name("테스터")
                .phone("010-1234-5678")
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);

        Shop shop = Shop.builder()
                .userId(savedUser.getId())
                .shopName("오픈마켓")
                .registrationNumber("123-45-67890")
                .description("질 좋은 상품만 판매합니다.")
                .bankName("한국은행")
                .accountNumber("110-123-456")
                .accountHolder("홍길동")
                .status(ShopStatus.WAITING)
                .build();

        Shop savedShop = shopRepository.save(shop);

        ShopApprovalCommand command = ShopApprovalCommand.builder()
                .decision(ApprovalDecision.APPROVE)
                .build();

        // when
        shopApprovalService.processApproval(savedShop.getId(), command);

        // then
        Shop updatedShop = shopRepository.findById(savedShop.getId()).orElseThrow();
        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        // 1. Shop 모듈 검증 (상태 변경)
        assertThat(updatedShop.getStatus()).isEqualTo(ShopStatus.ACTIVE);

        // 2. User 모듈 검증 (이벤트 리스너를 통한 권한 승격)
        assertThat(updatedUser.getRole()).isEqualTo(Role.ROLE_SELLER);
    }
}
