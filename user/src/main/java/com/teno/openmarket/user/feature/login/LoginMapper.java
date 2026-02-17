package com.teno.openmarket.user.feature.login;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LoginMapper {

    LoginCommand toCommand(LoginRequest request);
}