package com.lautaro.crud.dto;

import com.lautaro.entity.colegio.aula.clase.examen.enums.TipoEjercicio;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EjercicioDto {

    @NotBlank(message = "El enunciado esta vacio")
    private String enunciado;
    @NotBlank(message = "El tipo de ejercicio no puede estar vacio ")
    private TipoEjercicio tipoEjercicio;
    @NotBlank(message = "Puntaje maximo no puede estar vacio")
    private Double puntajeMaximo;
    @NotBlank(message = "Opcion no puede estar vacio")
    private OpcionDto opcionDto;
}
