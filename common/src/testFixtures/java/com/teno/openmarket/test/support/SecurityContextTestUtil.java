package com.teno.openmarket.test.support;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityContextTestUtil {
    public static SecurityContext createContext(long userId, String... roles) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        context.setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, authorities));
        return context;
    }
}