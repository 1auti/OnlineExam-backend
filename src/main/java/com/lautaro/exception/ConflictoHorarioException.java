package com.lautaro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictoHorarioException extends RuntimeException {
    public ConflictoHorarioException(String mensaje) {
        super(mensaje);
    }
}

