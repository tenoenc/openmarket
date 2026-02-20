package com.teno.openmarket.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증/인가가 필요한 API임을 명시하여,
 * Swagger 문서에 401/403 에러 코드를 자동으로 주입하기 위한 마커 어노테이션입니다.
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // 클래스 레벨에도 달 수 있게!
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityErrorDocs {
}