package com.teno.openmarket.shop.infra.shop;

import com.teno.openmarket.shop.domain.shop.Shop;
import com.teno.openmarket.shop.domain.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository jpaRepository;

    @Override
    public Shop save(Shop shop) {
        return jpaRepository.save(shop);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByShopName(String shopName) {
        return jpaRepository.existsByShopName(shopName);
    }
}
