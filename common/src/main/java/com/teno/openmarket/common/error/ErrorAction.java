package com.teno.openmarket.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorAction {
    TOAST("단순 알림"),
    DIALOG("차단/모달"),
    REDIRECT("페이지 이동"),
    REFRESH("새로고침"),
    NONE("조치 없음");

    private final String description;
}
