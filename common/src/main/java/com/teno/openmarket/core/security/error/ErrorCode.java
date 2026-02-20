package com.teno.openmarket.core.security.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    String getMessage();

    HttpStatus getStatus();

    ErrorAction getAction();
}
