package com.teno.openmarket.user.feature.address.update;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressUpdateMapper {
    AddressUpdateCommand toCommand(AddressUpdateRequest request);
}