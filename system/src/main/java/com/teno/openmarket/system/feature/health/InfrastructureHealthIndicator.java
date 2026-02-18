package com.teno.openmarket.system.feature.health;

public interface InfrastructureHealthIndicator {

    /**
     * 컴포넌트 이름 (예: "db", "redis")
     * <p>
     * HealthResponse의 필드명과 매핑하기 위해 사용합니다.
     */
    String getComponent();

    /**
     * 상태 점검
     * <p>
     * 각 구현체는 내부적으로 타임아웃 처리를 포함해야 함
     * @return 성공 시 true, 실패 시 false
     */
    boolean isUp();
}
