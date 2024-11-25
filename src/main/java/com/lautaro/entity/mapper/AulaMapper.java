package com.lautaro.entity.mapper;


import com.lautaro.crud.dto.AulaDto;
import com.lautaro.entity.aula.Aula;

public class AulaMapper {

    public static Aula toEntity(AulaDto aulaDto){
        Aula nuevaAula = new Aula();
        nuevaAula.setGrado(aulaDto.getGrado());
        nuevaAula.setAnio(aulaDto.getAnio());
        nuevaAula.setCapacidadMaxima(aulaDto.getCapacidadMaxima());
        nuevaAula.setModalidad(aulaDto.getModalidad());


        return nuevaAula;
    }
}
