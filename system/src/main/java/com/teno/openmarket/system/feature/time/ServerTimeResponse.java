package com.teno.openmarket.system.feature.time;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * [서버 시간 응답 DTO]
 * <p>
 * 타임딜 및 대기열의 공정성을 보장하기 위한 기준 서버 시간 정보를 담습니다.
 * 클라이언트는 이 정보를 바탕으로 로컬 시간과의 오차를 보정(Sync)해야 합니다.
 */
@Getter
@Builder
@Schema(name = "ServerTimeResponse")
public class ServerTimeResponse {

    /**
     * 서버 기준 시간 (KST)
     * <p>
     * 사람이 읽을 수 있는 날짜 및 시간 형식입니다. (예: 2026-02-18 10:00:00.123)
     */
    @Schema(example = "2026-02-18 10:00:00.123")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private final LocalDateTime serverTime;

    /**
     * 서버 기준 타임스탬프 (Epoch Millis)
     * <p>
     * 정밀한 시간 계산 및 타이머 동기화를 위한 Unix Timestamp (밀리초 단위)입니다.
     */
    @Schema(example = "1708218000123")
    private final long timestamp;
}