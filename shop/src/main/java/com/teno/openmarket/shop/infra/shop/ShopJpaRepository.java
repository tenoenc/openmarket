package com.teno.openmarket.shop.infra.shop;

import com.teno.openmarket.shop.domain.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<Shop, Long> {

    boolean existsByUserId(Long userId);

    boolean existsByShopName(String shopName);
}
