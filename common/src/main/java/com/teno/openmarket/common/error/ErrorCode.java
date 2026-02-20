package com.teno.openmarket.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();

    String getMessage();

    HttpStatus getStatus();

    ErrorAction getAction();
}
