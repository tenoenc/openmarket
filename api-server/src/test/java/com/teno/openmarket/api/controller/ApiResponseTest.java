package com.teno.openmarket.api.controller;

import com.teno.openmarket.common.config.JacksonConfig;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.GlobalResponseAdvice;
import com.teno.openmarket.common.response.ListWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiResponseTest.TestController.class)
@Import({
    GlobalResponseAdvice.class,
    JacksonConfig.class,
    ApiResponseTest.TestController.class
})
@TestPropertySource(properties = "spring.application.name=test-api-server")
public class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {

        // 단건 데이터 + BigDecimal 테스트
        @GetMapping("/test/response/single")
        public ApiResponse<Map<String, Object>> testSingle() {
            return ApiResponse.success(Map.of(
                    "amount", new BigDecimal("1000.50"),
                    "user", "tester"
            ));
        }

        // 리스트 데이터 테스트 (자동 래핑 확인)
        @GetMapping("/test/response/list")
        public ApiResponse<ListWrapper<String>> testList() {
            return ApiResponse.success(List.of("Apple", "Banana"));
        }
    }

    @Test
    @DisplayName("성공 응답: BigDecimal은 String으로 변환되고, 모듈명은 설정값과 일치해야 한다")
    void should_SerializeBigDecimalToString_When_ReturningSingleData() throws Exception {
        mockMvc.perform(get("/test/response/single"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.module").value("test-api-server"))
                .andExpect(jsonPath("$.data.amount").value("1000.50"))
                .andExpect(jsonPath("$.data.amount").isString());
    }

    @Test
    @DisplayName("리스트 응답: List<T> 반환 시 자동으로 { items: [] } 형태로 래핑되어야 한다")
    void should_WrapListInItemsField_When_ReturningList() throws Exception {
        mockMvc.perform(get("/test/response/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items[0]").value("Apple"))
                .andExpect(jsonPath("$.data.items[1]").value("Banana"));
    }

}
