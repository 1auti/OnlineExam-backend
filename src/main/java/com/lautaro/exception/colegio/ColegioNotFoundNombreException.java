package com.lautaro.exception.colegio;

public class ColegioNotFoundNombreException extends RuntimeException{
    public ColegioNotFoundNombreException(String nombre) {
        super("No se encontró el colegio con el nombre: " + nombre);
    }
}
