package com.teno.openmarket.core.security.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultType {
    SUCCESS("성공"),
    FAIL("실패"),
    PROCESSING("처리중"), // 비동기 작업 결과 조회 시 사용
    PARTIAL_SUCCESS("부분 성공"); // 대량 배치 처리 시 일부만 성공했을 때 사용

    private final String description;
}
