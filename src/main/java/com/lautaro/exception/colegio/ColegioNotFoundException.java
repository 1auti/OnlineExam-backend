package com.lautaro.exception.colegio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ColegioNotFoundException extends RuntimeException {
    public ColegioNotFoundException(Integer id) {
        super("No se encontró el colegio con id: " + id);
    }
}
