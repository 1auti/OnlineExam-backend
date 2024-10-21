package com.lautaro.exception.aula;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AulaVaciaException extends RuntimeException {
    public AulaVaciaException(String string) {
        super(string);
    }
}
