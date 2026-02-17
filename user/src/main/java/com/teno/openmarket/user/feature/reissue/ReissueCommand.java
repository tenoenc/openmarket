package com.teno.openmarket.user.feature.reissue;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueCommand {
    private final String refreshToken;
}
