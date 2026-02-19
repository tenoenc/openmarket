package com.teno.openmarket.user.feature.address.update;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressUpdateCommand {
    private final String addressName;
    private final String recipientName;
    private final String recipientPhone;
    private final String zipCode;
    private final String addressBase;
    private final String addressDetail;
}