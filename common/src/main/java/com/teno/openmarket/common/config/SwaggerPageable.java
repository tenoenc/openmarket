package com.teno.openmarket.common.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SwaggerPageable {

    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private Integer page;

    @Schema(description = "한 페이지당 조회할 데이터 개수", example = "10")
    private Integer size;

    @Schema(description = "정렬 조건 (예: id,desc)", example = "id,desc")
    private List<String> sort;
}
