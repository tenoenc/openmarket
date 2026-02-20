package com.teno.openmarket.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String LOCAL_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"; // ISO-8601 (Offset 포함)
    private static final String ZONED_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // ISO-8601 (Offset 포함)

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // JS의 부동소수점 오차 방지를 위해 금액은 문자열로 전송
            builder.serializerByType(BigDecimal.class, new ToStringSerializer());

            builder.timeZone(TimeZone.getTimeZone("Asia/Seoul"));
            builder.simpleDateFormat(DATE_FORMAT);

            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATETIME_FORMAT)));
            builder.serializers(new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(ZONED_DATETIME_FORMAT)));

            // 클라이언트가 DTO에 없는 필드를 보내도 에러를 내지 않고 무시함
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            // 날짜를 타임스탬프가 아닌 문자열로 출력
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }

}
