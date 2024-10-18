package com.lautaro.exception.colegio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CapacidadColegioExcedidaException extends RuntimeException {
    public CapacidadColegioExcedidaException(String mensaje) {
        super(mensaje);
    }
}
