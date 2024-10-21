package com.lautaro.entity.mapper;

import com.lautaro.crud.dto.ExamenDto;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;

public class ExamenMapper {

    public static Examen toEntity(ExamenDto examenDto){
        Examen examen = new Examen();
        examen.setFechaExamen(examenDto.getFechaExamen());
        examen.setDificultad(examenDto.getDificultad());

        return examen;
    }


}
