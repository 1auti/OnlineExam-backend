package com.lautaro.exception.examen;

public class ExamenAlreadyGradedException extends RuntimeException {
    public ExamenAlreadyGradedException(String message) {
        super(message);
    }
}
