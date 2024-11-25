package com.lautaro.crud.dto;

import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.recursos.Formato;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecursoDto {
    @NotBlank(message = "El nombre del recurso no puede estar vacio")
    private String nombreRecurso;
    @NotBlank(message = "El formato del recurso no puede estar vacio")
    private Formato formato;
    @NotBlank(message = "La materia no puede estar vacia")
    private Integer materiaId;
    @NotBlank(message = "El archivo no puede estar vacio")
    private MultipartFile archivo;
    @NotBlank(message = "La ruta no puede estar vacia")
    private String ruta;
}
