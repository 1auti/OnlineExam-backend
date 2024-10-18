package com.lautaro.exception.aula;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AulaLlenaException extends RuntimeException {
    public AulaLlenaException(String mensaje) {
        super(mensaje);
    }
}
