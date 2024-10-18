package com.lautaro.exception.clase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClaseNotFoundException extends RuntimeException {
    public ClaseNotFoundException(Integer id) {
        super("No se encontró la clase con id: " + id);
    }
}
