package com.teno.openmarket.shop.domain.shop;

import java.util.Optional;

public interface ShopRepository {

    Shop save(Shop shop);

    boolean existsByUserId(Long userId);

    boolean existsByShopName(String shopName);

    Optional<Shop> findById(Long id);
}
