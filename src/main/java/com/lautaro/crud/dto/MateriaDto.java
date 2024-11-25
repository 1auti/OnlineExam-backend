package com.lautaro.crud.dto;

import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.recursos.Recurso;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MateriaDto {

    @NotBlank(message = "El nombre de la materia no puede estar vacio")
    private String nombreMateria;
    @NotBlank(message = "El profesor no puede estar vacio")
    private Profesor profesor;
    @NotBlank(message = "El colegio no puede estar vacio")
    private Colegio colegio;
    @NotBlank(message = "Al menos debe tener algun recurso")
    private List<Recurso> recursos;


}
