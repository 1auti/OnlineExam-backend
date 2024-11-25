package com.lautaro.entity.mapper;

import com.lautaro.crud.dto.EjercicioDto;
import com.lautaro.entity.examen.Ejercicio;

public class EjercicioMapper {

    public static Ejercicio toEntity(EjercicioDto ejercicioDto){
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setTipoEjercicio(ejercicioDto.getTipoEjercicio());
        ejercicio.setEnunciado(ejercicioDto.getEnunciado());
        ejercicio.setPuntajeMaximo(ejercicioDto.getPuntajeMaximo());
        return ejercicio;
    }
}
