package com.teno.openmarket.core.security.response;

import java.util.List;

/**
 * API 응답 시 List<T>를 바로 반환하지 않고, {"items": [...]} 형태로 감싸기 위한 래퍼
 */
public record ListWrapper<T>(List<T> items) {

    public static <T> ListWrapper<T> of(List<T> items) {
        return new ListWrapper<>(items);
    }
}
