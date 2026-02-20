package com.teno.openmarket.core.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JacksonConfig.class, JacksonAutoConfiguration.class})
public class JacksonConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    record TestModel(
            BigDecimal price,
            LocalDate date,
            LocalDateTime localDateTime,
            ZonedDateTime zonedDateTime
    ) {}

    record SimpleModel(String name) {}

    @Test
    @DisplayName("객체가 BigDecimal 필드 소유 시 BigDecimal은 반드시 String으로 직렬화되어야 한다")
    void should_SerializeBigDecimalToString_When_ObjectContainsBigDecimalField() throws JsonProcessingException {
        // given
        TestModel model = new TestModel(
                new BigDecimal("1000.50"),
                LocalDate.now(),
                LocalDateTime.now(),
                ZonedDateTime.now()
        );

        // when
        String json = objectMapper.writeValueAsString(model);

        // then
        assertThat(json).contains("\"price\":\"1000.50\"");
        assertThat(json).doesNotContain("\"price\":1000.50");
    }

    @Test
    @DisplayName("객체가 시간 필드 소유 시 날짜는 yyyy-MM-dd, 시간은 ISO-8601 포맷이어야 한다")
    void should_SerializeDateToIsoFormat_When_ObjectContainsTimeFields() throws JsonProcessingException {
        // given
        LocalDate date = LocalDate.of(2026, 2, 15);
        LocalDateTime localDateTime = LocalDateTime.of(2026, 2, 15, 13, 30, 0);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Asia/Seoul"));
        TestModel model = new TestModel(BigDecimal.ZERO, date, localDateTime, zonedDateTime);

        // when
        String json = objectMapper.writeValueAsString(model);

        // then
        assertThat(json).contains("\"date\":\"2026-02-15\"");
        assertThat(json).contains("\"localDateTime\":\"2026-02-15T13:30:00.000\"");
        assertThat(json).contains("\"zonedDateTime\":\"2026-02-15T13:30:00.000+09:00\"");
    }

    @Test
    @DisplayName("JSON 역직렬화 시 알 수 없는 필드가 있어도 에러 없이 무시해야 한다")
    void should_IgnoreUnknownProperties_When_DeserializingJsonWithExtraFields() throws JsonProcessingException {
        // given
        String jsonWithUnknown = """
                {
                    "name": "test",
                    "unknownFields": "I am hacker",
                    "anotherUnknown": 12345
                }
                """;

        // when
        SimpleModel result = objectMapper.readValue(jsonWithUnknown, SimpleModel.class);

        // then
        assertThat(result.name()).isEqualTo("test");
    }

    @Test
    @DisplayName("직렬화 설정을 확인 시 KST 시간대가 적용되어야 한다")
    void should_UseAsiaSeoulTimezone_When_CheckingSerializationConfig() {
        // when
        TimeZone timeZone = objectMapper.getSerializationConfig().getTimeZone();

        // then
        assertThat(timeZone).isEqualTo(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
