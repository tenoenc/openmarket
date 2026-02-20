package com.teno.openmarket.core.security.event;

import java.util.Map;

/**
 * [모듈 간 통신을 위한 범용 도메인 이벤트]
 * <p>
 * 특정 도메인의 클래스에 직접 의존하지 않고
 * 모듈 간 느슨한 결합을 유지하기 위해 사용하는 공통 이벤트 객체입니다.
 *
 * @param eventType 이벤트의 종류 (예: "SHOP_APPROVED")
 * @param payload   이벤트와 함께 전달할 핵심 데이터
 */
public record DomainEvent(
        String eventType,
        Map<String, Object> payload
) {
}