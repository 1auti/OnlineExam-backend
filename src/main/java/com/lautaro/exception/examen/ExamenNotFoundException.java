package com.lautaro.exception.examen;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExamenNotFoundException extends RuntimeException {
    public ExamenNotFoundException(Integer id) {
        super("No se encontró el examen con id: " + id);
    }
}
