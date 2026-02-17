package com.teno.openmarket.user.feature.reissue;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReissueMapper {
    ReissueCommand toCommand(ReissueRequest request);
}
