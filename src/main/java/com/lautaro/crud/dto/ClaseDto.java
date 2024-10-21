package com.lautaro.crud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClaseDto {

    @NotBlank(message = "La materia esta vacia")
    private String materia;
    @NotBlank(message = "El Aula no puede estar vacia")
    private AulaDto aulaDto;
    @NotBlank(message = "El Email del profesor no puede estar vacio")
    private String emailProfesor;
    @NotBlank(message = "La Hora de inicio no puede estar vacia")
    private LocalDateTime horaInicio;
    @NotBlank(message = "La Hora de fin no puede estar vacia")
    private LocalDateTime horaFin;
    @NotBlank(message = "La Fecha no puede estar vacia")
    private LocalDate fecha;

}

