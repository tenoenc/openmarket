package com.teno.openmarket.shop.feature.apply;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopApplyMapper {
    ShopApplyCommand toCommand(ShopApplyRequest request);
}
