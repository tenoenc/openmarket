package com.teno.openmarket.shop.feature.approval;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopApprovalMapper {
    ShopApprovalCommand toCommand(ShopApprovalRequest request);
}
