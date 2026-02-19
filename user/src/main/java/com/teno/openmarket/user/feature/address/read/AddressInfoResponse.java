package com.teno.openmarket.user.feature.address.read;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "AddressInfoResponse")
public class AddressInfoResponse {

    private Long id;
    private String addressName;
    private String recipientName;
    private String recipientPhone;
    private String zipCode;
    private String addressDetail;
    private boolean isDefault;
}
