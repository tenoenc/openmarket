package com.teno.openmarket.user.feature.profile.update;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileUpdateMapper {

    ProfileUpdateCommand toCommand(ProfileUpdateRequest request);
}
