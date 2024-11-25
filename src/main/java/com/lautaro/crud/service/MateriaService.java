package com.lautaro.crud.service;

import com.lautaro.crud.dto.MateriaDto;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.recursos.Recurso;

import java.util.List;

public interface MateriaService{
    Materia crearMateria(MateriaDto materiaDto);
    void eliminarMateria(Integer materiaId);
    Materia buscarMateria(String nombreMateria, Clase clase);
    Materia editarMateria(Materia materia);
    Estudiante asignarEstudiante(Estudiante estudiante,Materia materia);
    void removerEstudiante(Integer estudianteId,Materia materia);
    List<Recurso> traerRecursos(Integer materiaId);
    List<Materia> traerExamenes(Integer materiaId);
}
