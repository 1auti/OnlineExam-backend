package com.lautaro.entity.mapper;


import com.lautaro.crud.dto.EstudianteDto;
import com.lautaro.entity.persona.estudiante.Estudiante;

public class EstudianteMapper {

    public static Estudiante toEntity(EstudianteDto dto) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(dto.getNombre());
        estudiante.setApellido(dto.getApellido());
        estudiante.setEdad(dto.getEdad());
        estudiante.setTel(dto.getTel());
        estudiante.setEmail(dto.getEmail());
        return estudiante;
    }
}
