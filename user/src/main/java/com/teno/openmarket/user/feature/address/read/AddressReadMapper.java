package com.teno.openmarket.user.feature.address.read;

import com.teno.openmarket.user.domain.address.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressReadMapper {

    AddressInfoResponse toResponse(UserAddress userAddress);
}
