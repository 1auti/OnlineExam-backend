package com.lautaro.crud.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColegioDto {

    @NotBlank(message = "El nombre del colegio no puede estar vacia")
    private String nombre;
    @NotBlank(message = "La dirrecion del colegio no puede estar vacia")
    private String direccion;
}
