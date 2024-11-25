package com.lautaro.entity.mapper;

import com.lautaro.crud.dto.MateriaDto;
import com.lautaro.entity.materia.Materia;

public class MateriaMapper {

    public static Materia toEntity(MateriaDto materiaDto){
        Materia materia = new Materia();
        materia.setNombreMateria(materiaDto.getNombreMateria());
        materia.setProfesor(materiaDto.getProfesor());
        materia.setColegio(materiaDto.getColegio());
        materia.setRecursos(materiaDto.getRecursos());
        return materia;
    }
}
