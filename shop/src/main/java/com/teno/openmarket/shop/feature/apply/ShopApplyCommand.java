package com.teno.openmarket.shop.feature.apply;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopApplyCommand {
    private final String shopName;
    private final String registrationNumber;
    private final String description;
    private final String bankName;
    private final String accountNumber;
    private final String accountHolder;
}