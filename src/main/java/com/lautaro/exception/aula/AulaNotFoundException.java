package com.lautaro.exception.aula;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AulaNotFoundException extends RuntimeException {
    public AulaNotFoundException(String string) {
        super(string);
    }
}
