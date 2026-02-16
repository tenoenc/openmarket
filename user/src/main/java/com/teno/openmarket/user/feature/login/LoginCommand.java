package com.teno.openmarket.user.feature.login;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {
    private String email;
    private String password;
}
