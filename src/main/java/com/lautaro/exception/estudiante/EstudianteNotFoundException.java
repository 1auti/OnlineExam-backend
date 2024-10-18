package com.lautaro.exception.estudiante;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EstudianteNotFoundException extends RuntimeException {
    public EstudianteNotFoundException(Long id) {
        super("No se encontró el estudiante con id: " + id);
    }
}
