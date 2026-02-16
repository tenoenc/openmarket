package com.teno.openmarket.user.feature.auth.mapper;

import com.teno.openmarket.user.feature.auth.dto.SignupRequest;
import com.teno.openmarket.user.feature.auth.dto.SignupCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring", // 스프링 빈으로 등록 (@Component)
    unmappedTargetPolicy = ReportingPolicy.IGNORE // 매핑 안 된 필드 있어도 에러 안 냄
)
public interface UserMapper {

    // 정적 팩토리 방식
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    SignupCommand toCommand(SignupRequest request);
}