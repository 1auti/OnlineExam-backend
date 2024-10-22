package com.lautaro.crud.service;


import com.lautaro.crud.dto.ProfesorDto;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ProfesorService {

    Profesor crearProfesor(ProfesorDto profesor);
    Profesor buscarProfesorPorId(Integer id);
    List<Profesor> buscarTodosProfesores();
    Profesor actualizarProfesor(Profesor profesor);
    List<Profesor> trarProfesoresPorColegio(Colegio colegio);
    void eliminarProfesor(Integer id);
    void asignarClase(Integer profesorId, Integer claseId);
    void removerClase(Integer profesorId, Integer claseId);
    List<Clase> buscarClasesDeProfesor(Integer profesorId);

    int calcularCargaHoraria(Integer profesorId);
    Map<String, Integer> calcularCargaHorariaPorMateria(Integer profesorId);
    boolean verificarDisponibilidad(Integer profesorId, LocalTime inicio, LocalTime fin);
    List<Profesor> buscarProfesoresPorMateria(String materia);
}
