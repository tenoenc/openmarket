package com.teno.openmarket.user.feature.profile.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileUpdateCommand {
    private final String name;
    private final String phone;
}
