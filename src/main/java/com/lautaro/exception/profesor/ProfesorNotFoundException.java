package com.lautaro.exception.profesor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfesorNotFoundException extends RuntimeException {
    public ProfesorNotFoundException(String mensaje) {
        super(mensaje);
    }
}
