package com.teno.openmarket.user.feature.address.create;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressCreateMapper {
    AddressCreateCommand toCommand(AddressCreateRequest request);
}
