package com.lautaro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CapacidadAulaExcedidaException extends RuntimeException {
    public CapacidadAulaExcedidaException(String mensaje) {
        super(mensaje);
    }
}
