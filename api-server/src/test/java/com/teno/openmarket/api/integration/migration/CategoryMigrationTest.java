package com.teno.openmarket.api.integration.migration;

import com.teno.openmarket.api.integration.ApiIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryMigrationTest extends ApiIntegrationTest {

    @Test
    @DisplayName("V2 마이그레이션 실행 후 카테고리 초기 데이터가 존재해야 한다")
    void should_ExistCategoryData_When_V2MigrationExecuted() {
        // when
        // BaseIntegrationTest 구동 시 Flyway가 이미 실행됨

        // then
        // 1. 전체 카테고리 개수 검증 (예: 상위 4개 + 하위 8개 = 총 12개 이상)
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM categories", Integer.class
        );

        assertThat(count).isGreaterThanOrEqualTo(10);

        // 2. 주요 카테고리 존재 여부 검증
        List<Map<String, Object>> rootCategories = jdbcTemplate.queryForList(
                "SELECT name FROM categories WHERE parent_id IS NULL"
        );

        assertThat(rootCategories).extracting("name")
                .contains("전자제품", "의류", "식품", "도서");

        // 3. 하위 카테고리 계층 구조 검증 (전자제품의 자식 카테고리 확인)
        List<Map<String, Object>> subCategories = jdbcTemplate
                .queryForList("SELECT name FROM categories WHERE parent_id = 1");

        assertThat(subCategories).extracting("name")
                .contains("스마트폰", "노트북");
    }
}
