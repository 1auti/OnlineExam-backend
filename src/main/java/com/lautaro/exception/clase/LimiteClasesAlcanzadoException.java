package com.lautaro.exception.clase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LimiteClasesAlcanzadoException extends RuntimeException {
    public LimiteClasesAlcanzadoException(String mensaje) {
        super(mensaje);
    }
}
