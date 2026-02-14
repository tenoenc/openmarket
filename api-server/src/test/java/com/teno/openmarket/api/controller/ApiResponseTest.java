package com.teno.openmarket.api.controller;

import com.teno.openmarket.common.config.JacksonConfig;
import com.teno.openmarket.common.exception.GlobalExceptionHandler;
import com.teno.openmarket.common.response.ApiResponse;
import com.teno.openmarket.common.response.GlobalResponseAdvice;
import com.teno.openmarket.common.response.ListWrapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiResponseTest.TestController.class)
@Import({
    GlobalResponseAdvice.class,
    GlobalExceptionHandler.class,
    JacksonConfig.class,
    ApiResponseTest.TestController.class
})
@TestPropertySource(properties = "spring.application.name=test-api-server")
public class ApiResponseTest {

    @Autowired
    private MockMvc mockMvc;

    record TestRequest(@NotBlank(message = "이름은 필수입니다") String name) {}

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

        // 유효성 검사 테스트
        @PostMapping("/test/response/validation")
        public ApiResponse<Void> testValidation(@Valid @RequestBody TestRequest request) {
            return ApiResponse.success();
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

    @Test
    @DisplayName("헤더: X-Request-ID 헤더가 있으면 응답의 requestId로 바인딩되어야 한다")
    void should_BindRequestId_When_HeaderIsPresent() throws Exception {
        String clientRequestId = "req-custom-1234";

        mockMvc.perform(get("/test/response/single")
                        .header("X-Request-ID", clientRequestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(clientRequestId));
    }

    @Test
    @DisplayName("유효성 검사 실패: errors 배열에 필드명과 사유가 포함되어야 한다")
    void should_ReturnErrorList_When_ValidationFails() throws Exception {
        String invalidJson = "{\"name\": \"\"}";

        mockMvc.perform(post("/test/response/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.[0].field").value("name"))
                .andExpect(jsonPath("$.errors.[0].reason").value("이름은 필수입니다"));
    }
}
