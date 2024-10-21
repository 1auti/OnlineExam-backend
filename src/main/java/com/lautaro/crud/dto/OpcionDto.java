package com.lautaro.crud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OpcionDto {

    @NotBlank(message = "El texto no puede estar vacio")
    private String texto;

}
