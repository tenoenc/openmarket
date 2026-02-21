package com.teno.openmarket.shop.feature.approval;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShopApprovalCommand {
    private ApprovalDecision decision;
    private String rejectReason;
}
