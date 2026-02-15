package com.teno.openmarket.user.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 인프라 설정
 * <p>
 * 사용자 도메인(User)의 세션 관리 및 캐싱 처리를 위한 Redis 구성을 담당합니다.
 * </p>
 * <ul>
 * <li>Client: Lettuce</li>
 * <li>Serialization
 * <ul>
 * <li>Key: String</li>
 * <li>Value: JSON</li>
 * </ul>
 * </li>
 * </ul>
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * Redis 연결 팩토리 (Lettuce) 설정
     * <p>
     * Jedis 대비 스레드 안전하며, Netty 기반의 비동기 처리를 지원하여
     * 고성능 환경에 적합한 LettuceConnectionFactory를 사용합니다.
     * </p>
     *
     * @return RedisConnectionFactory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * RedisTemplate 설정
     * <p>
     * 기본 {@code JdkSerializationRedisSerializer} 대신 JSON 직렬화를 사용하여
     * 데이터의 가독성을 높이고 타 언어/서비스와의 호환성을 확보합니다.
     * </p>
     *
     * <b>직렬화 전략</b>
     * <ul>
     * <li>Key: {@link StringRedisSerializer} - redis-cli 등에서 키 식별 용이</li>
     * <li>Value: {@link GenericJackson2JsonRedisSerializer} - 객체 클래스 타입 정보를 포함하여 저장.
     * 역직렬화 시 별도의 캐스팅 없이 원본 객체로 복원 가능.</li>
     * </ul>
     *
     * @return 구성된 RedisTemplate (Key: String, Value: Object)
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 연결 팩토리 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key 직렬화: 일반 String으로 저장
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Value 직렬화: JSON 포맷 + @class 메타데이터 저장
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);

        return redisTemplate;
    }
}