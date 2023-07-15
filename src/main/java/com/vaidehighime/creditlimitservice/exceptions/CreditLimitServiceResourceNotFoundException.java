package com.vaidehighime.creditlimitservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class CreditLimitServiceResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CreditLimitServiceResourceNotFoundException(String message) {
        super(message);
    }
}
