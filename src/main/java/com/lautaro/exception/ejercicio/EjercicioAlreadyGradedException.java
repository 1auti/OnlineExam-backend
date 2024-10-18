package com.lautaro.exception.ejercicio;

public class EjercicioAlreadyGradedException extends RuntimeException {
    public EjercicioAlreadyGradedException(String message) {
        super(message);
    }
}