package com.lautaro.entity.mapper;

import com.lautaro.crud.dto.ClaseDto;
import com.lautaro.entity.colegio.aula.clase.Clase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClaseMapper {

    public static Clase toEntity(ClaseDto claseDto){
        Clase clase = new Clase();
        clase.setMateria(claseDto.getMateria());
        clase.setHoraFin(claseDto.getHoraFin());
        clase.setHoraInicio(claseDto.getHoraInicio());
        clase.setFechaClase(claseDto.getFecha());

        return clase;
    }
}
