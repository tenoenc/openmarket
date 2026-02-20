package com.teno.openmarket.core.security.annotation;

import com.teno.openmarket.core.security.error.GlobalErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalErrorCodeExamples {
    GlobalErrorCode[] value();
}
