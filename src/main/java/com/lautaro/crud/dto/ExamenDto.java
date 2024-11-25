package com.lautaro.crud.dto;

import com.lautaro.entity.examen.enums.Dificultad;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ExamenDto {

    @NotBlank(message = "La dificultad esta vacia")
    private Dificultad dificultad;
    @NotBlank(message = "Profesor no puede estar vacio")
    private Integer profesorId;
    @NotBlank(message = "La fecha del examen esta vacia")
    private LocalDateTime fechaExamen;
    @NotBlank(message = "La clase no puede estar vacia")
    private ClaseDto clase;



}
