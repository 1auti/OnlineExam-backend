package com.lautaro.entity.mapper;


import com.lautaro.crud.dto.RecursoDto;
import com.lautaro.entity.recursos.Recurso;

public class RecursoMapper {

    public static Recurso toEntity(RecursoDto recursoDto){
        Recurso recurso = new Recurso();
        recurso.setNombre(recursoDto.getNombreRecurso());
        recurso.setFormato(recursoDto.getFormato());
        recurso.setMateria(recursoDto.getMateria());
        recurso.setRuta(recursoDto.getRuta());
        return recurso;
    }
}
