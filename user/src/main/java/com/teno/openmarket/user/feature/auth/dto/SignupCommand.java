package com.teno.openmarket.user.feature.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * [회원가입 명령 객체]
 * <p>
 * Controller(API)에서 Service(Domain)로 데이터를 전달할 때 사용하는 순수 POJO입니다.
 * {@code SignupRequest}와의 의존성을 끊기 위해 사용합니다.
 */
@Getter
@Builder
public class SignupCommand {
    private final String email;
    private final String password;
    private final String name;
    private final String phone;
    private final List<Long> termIds;
}
