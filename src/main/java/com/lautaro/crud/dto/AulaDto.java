package com.lautaro.crud.dto;

import com.lautaro.entity.aula.enums.Grado;
import com.lautaro.entity.aula.enums.Modalidad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulaDto {

    @NotBlank(message = "Año maxima no puede estar vacia")
    private Integer anio;

    @NotBlank(message = "Grado maxima no puede estar vacia")
    private Grado grado;

    @NotBlank(message = "Modalidad maxima no puede estar vacia")
    private Modalidad modalidad;

    @NotBlank(message = "Capacidad maxima no puede estar vacia")
    private String nombreColegio;

    @Positive
    @Max(30)
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    @NotBlank(message = "Capacidad maxima no puede estar vacia")
    private Integer capacidadMaxima;
}
