package com.teno.openmarket.system.domain.time;

/**
 * [시간 동기화 예외]
 * <p>
 * 기준 시간(Source of Truth)을 가져오는 데 실패했을 때 발생하는 도메인 예외입니다.
 * 인프라(Redis, DB 등)의 구체적인 오류를 이 예외로 추상화하여 상위 계층으로 전달합니다.
 */
public class TimeSynchronizationException extends RuntimeException {
    public TimeSynchronizationException(String message) {
        super(message);
    }

    public TimeSynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
