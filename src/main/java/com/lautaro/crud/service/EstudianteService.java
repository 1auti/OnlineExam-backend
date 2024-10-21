package com.lautaro.crud.service;

import com.lautaro.crud.dto.EstudianteDto;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EstudianteService {
    Estudiante crearEstudiante(EstudianteDto estudianteDto);
    boolean eliminarEstudiante(Integer id);
    Estudiante actualizarEstudiante(Long cardId, EstudianteDto estudianteDto);
    void calcularPromedio(Estudiante estudiante);
    void agregarExamen(Estudiante estudiante, Examen examen);
    void removerExamen(Estudiante estudiante, Examen examen);
    Map<String, Double> calcularPromedioPorMateria(Estudiante estudiante);
    List<Estudiante> buscarEstudiantes();
    void asignarEstudianteAula(Estudiante estudiante, Aula aula);
    void removerEstudianteDeAula(Estudiante estudiante);
    void actualizarRanking(Colegio colegio);
    Estudiante buscarEstudiantePorCardId(Long cardId, Integer colegioId);
    Map<String, Double> obtenerRendimientoComparativo(Estudiante estudiante);
    List<Estudiante> buscarEstudiantesPorRendimiento(Colegio colegio, double promedioMinimo);
}
