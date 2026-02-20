package com.teno.openmarket.test.support;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserId.Factory.class)
public @interface WithMockUserId {

    long value() default 1L;

    class Factory implements WithSecurityContextFactory<WithMockUserId> {
        @Override
        public SecurityContext createSecurityContext(WithMockUserId annotation) {
            return SecurityContextTestUtil.createContext(annotation.value(), "USER");
        }
    }
}
