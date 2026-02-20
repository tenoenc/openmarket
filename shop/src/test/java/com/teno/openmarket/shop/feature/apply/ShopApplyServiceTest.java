package com.teno.openmarket.shop.feature.apply;

import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.shop.domain.exception.ShopErrorCode;
import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import com.teno.openmarket.shop.domain.shop.ShopStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ShopApplyServiceTest {

    @InjectMocks
    private ShopApplyService shopApplyService;

    @Mock
    private ShopRepository shopRepository;

    @Test
    @DisplayName("정상적인 입점 신청 시 상태가 WAITING인 상점이 생성되어야 한다")
    void should_CreateShopWithWaitingStatus_When_ValidApplication() {
        // given
        Long userId = 1L;
        ShopApplyCommand command = ShopApplyCommand.builder()
                .shopName("오픈마켓")
                .registrationNumber("123-45-67890")
                .description("소개글")
                .bankName("한국은행")
                .accountNumber("110-123-456")
                .accountHolder("홍길동")
                .build();

        given(shopRepository.existsByUserId(userId)).willReturn(false);
        given(shopRepository.existsByShopName(command.getShopName())).willReturn(false);

        // when
        shopApplyService.apply(userId, command);

        // then
        ArgumentCaptor<Shop> captor = ArgumentCaptor.forClass(Shop.class);
        verify(shopRepository).save(captor.capture());

        Shop savedShop = captor.getValue();
        assertThat(savedShop.getStatus()).isEqualTo(ShopStatus.WAITING);
        assertThat(savedShop.getShopName()).isEqualTo("오픈마켓");
        assertThat(savedShop.getRegistrationNumber()).isEqualTo("123-45-67890");
    }

    @Test
    @DisplayName("이미 상점을 보유한 유저가 신청 시 예외가 발생해야 한다")
    void should_ThrowException_When_UserAlreadyHasShop() {
        // given
        Long userId = 1L;
        ShopApplyCommand command = ShopApplyCommand.builder().shopName("오픈마켓").build();

        given(shopRepository.existsByUserId(userId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> shopApplyService.apply(userId, command))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ShopErrorCode.SHOP_ALREADY_EXISTS.getMessage());
    }
}
