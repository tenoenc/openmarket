package com.teno.openmarket.shop.feature.approval;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [상점 입점 승인/반려 요청 DTO]
 * <p>
 * 관리자가 대기 중인 상점의 입점 신청을 승인하거나 반려할 때 사용하는 객체입니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ShopApprovalRequest")
public class ShopApprovalRequest {

    /**
     * 승인/반려 결정
     * <p>
     * 관리자의 승인(APPROVE) 또는 반려(REJECT) 결정 여부입니다.
     */
    @NotNull(message = "승인/반려 결정은 필수입니다.")
    private ApprovalDecision decision;

    /**
     * 반려 사유
     * <p>
     * 반려 결정 시 입력하는 상세 사유입니다. 승인 결정인 경우 이 값은 무시됩니다.
     */
    private String rejectReason;
}
