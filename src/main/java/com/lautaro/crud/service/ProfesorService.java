package com.lautaro.crud.service;


import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProfesorService {

    Profesor crearProfesor(Profesor profesor);
    Profesor buscarProfesorPorId(Integer id);
    List<Profesor> buscarTodosProfesores();
    Profesor actualizarProfesor(Profesor profesor);
    void eliminarProfesor(Integer id);

    void asignarClase(Integer profesorId, Integer claseId);
    void removerClase(Integer profesorId, Integer claseId);
    List<Clase> buscarClasesDeProfesor(Integer profesorId);

    int calcularCargaHoraria(Integer profesorId);
    Map<String, Integer> calcularCargaHorariaPorMateria(Integer profesorId);
    boolean verificarDisponibilidad(Integer profesorId, LocalDateTime inicio, LocalDateTime fin);
    List<Profesor> buscarProfesoresPorMateria(String materia);
}
