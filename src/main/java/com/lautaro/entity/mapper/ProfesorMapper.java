package com.lautaro.entity.mapper;


import com.lautaro.crud.dto.ProfesorDto;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;

public class ProfesorMapper {

    public static Profesor toEntity(ProfesorDto profesorDto){
        Profesor profesor = new Profesor();
        profesor.setNombre(profesorDto.getNombre());
        profesor.setApellido(profesorDto.getApellido());
        profesor.setEmail(profesorDto.getEmail());
        profesor.setEdad(profesorDto.getEdad());
        profesor.setTel(profesorDto.getTel());
        profesor.setTitulo(profesorDto.getTitulo());
        profesor.setDepartamento(profesorDto.getDepartamento());

        return profesor;
    }
}
