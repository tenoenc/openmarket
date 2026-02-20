package com.teno.openmarket.common.response;

import com.teno.openmarket.common.error.GlobalErrorCode;
import com.teno.openmarket.common.exception.BusinessException;
import com.teno.openmarket.test.support.BaseControllerTest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@Import(ApiResponseTest.TestController.class)
@TestPropertySource(properties = "spring.application.name=test-common")
public class ApiResponseTest extends BaseControllerTest {

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

        // 비즈니스 예외 발생 테스트
        @GetMapping("/test/response/business-error")
        public ApiResponse<Void> testBusinessError() {
            throw new BusinessException(GlobalErrorCode.SYSTEM_ERROR);
        }
    }

    @Test
    @DisplayName("단건 데이터 응답 시 BigDecimal은 String으로 변환되고, 모듈명은 설정값과 일치해야 한다")
    void should_SerializeBigDecimalToString_When_ReturningSingleData() throws Exception {
        mockMvc.perform(get("/test/response/single"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.module").value("test-common"))
                .andExpect(jsonPath("$.data.amount").value("1000.50"))
                .andExpect(jsonPath("$.data.amount").isString());
    }

    @Test
    @DisplayName("리스트 응답 시 List<T> 반환 시 자동으로 { items: [] } 형태로 래핑되어야 한다")
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
    @DisplayName("X-Request-ID 헤더가 있으면 응답의 requestId로 바인딩되어야 한다")
    void should_BindRequestId_When_HeaderIsPresent() throws Exception {
        String clientRequestId = "req-custom-1234";

        mockMvc.perform(get("/test/response/single")
                        .header("X-Request-ID", clientRequestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(clientRequestId));
    }

    @Test
    @DisplayName("유효성 검사 실패 시 errors 배열에 필드명과 사유가 포함되어야 한다")
    void should_ReturnErrorList_When_ValidationFails() throws Exception {
        String invalidJson = "{\"name\": \"\"}";

        mockMvc.perform(post("/test/response/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.[0].field").value("name"))
                .andExpect(jsonPath("$.errors.[0].reason").value("이름은 필수입니다"));
    }

    @Test
    @DisplayName("비즈니스 예외 발생 시 정의된 ErrorCode와 메시지가 JSON에 매핑되어야 한다")
    void should_ReturnCorrectErrorCode_When_BusinessExceptionThrown() throws Exception {
        mockMvc.perform(get("/test/response/business-error"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.result").value("FAIL"))
                .andExpect(jsonPath("$.errorCode").value("SYSTEM_ERROR"))
                .andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다."));
    }

    @Test
    @DisplayName("클라이언트가 X-Request-ID를 보내면 응답 헤더와 바디에 그대로 반환되어야 한다 (Echo)")
    void should_EchoTraceId_When_ClientProvidesRequestId() throws Exception {
        // given
        String clientRequestId = "req-client-custom-id";

        // when & then
        mockMvc.perform(get("/test/response/single")
                        .header("X-Request-ID", clientRequestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-ID", clientRequestId))
                .andExpect(jsonPath("$.traceId").value(clientRequestId))
                .andExpect(jsonPath("$.requestId").value(clientRequestId));
    }

    @Test
    @DisplayName("헤더 없이 요청하면 서버가 새로운 UUID를 생성하여 반환해야 한다")
    void should_GenerateNewTraceId_When_HeaderIsMissing() throws Exception {
        // when & then
        mockMvc.perform(get("/test/response/single"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-ID"))
                .andExpect(jsonPath("$.traceId").exists())
                .andExpect(jsonPath("$.traceId").isNotEmpty());
    }
}