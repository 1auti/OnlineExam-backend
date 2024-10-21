package com.lautaro.crud.dto;

import com.lautaro.entity.persona.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesorDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    @NotBlank(message = "El Telefono no puede estar vacío")
    private String tel;
    @NotBlank(message = "El Sexo no puede estar vacío")
    private Sexo sexo;
    @Email
    @NotBlank(message = "El Email no puede estar vacío")
    private String email;
    @NotBlank(message = "La Edad no puede estar vacío")
    private Integer edad;
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;
    @NotBlank(message = "El departamento no puede estar vacio")
    private String departamento;
    @NotBlank(message = "El Nombre del Colegio no puede estar vacio")
    private String nombreColegio;
}
