package com.lautaro.entity.mapper;

import com.lautaro.crud.dto.OpcionDto;
import com.lautaro.entity.examen.Opcion;

public class OpcionMapper {

    public static Opcion toEntity(OpcionDto opcionDto){
        Opcion opcion = new Opcion();
        opcion.setTexto(opcionDto.getTexto());
        return opcion;
    }
}
