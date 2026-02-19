package com.teno.openmarket.user.feature.address.create;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressCreateCommand {
    private final String addressName;
    private final String recipientName;
    private final String recipientPhone;
    private final String zipCode;
    private final String addressBase;
    private final String addressDetail;
}
