package com.lautaro.crud.service;

import com.lautaro.crud.dto.ExamenDto;
import com.lautaro.entity.colegio.aula.clase.examen.Ejercicio;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;

import java.time.LocalDate;
import java.util.List;

public interface ExamenService {

    Examen crearExamen(ExamenDto examen);
    Examen obtenerExamenPorId(Integer id);
    List<Examen> obtenerTodosLosExamenes();
    Examen actualizarExamen(Integer id, Examen examenActualizado);
    void eliminarExamen(Integer id);
    void agregarEjercicioAExamen(Integer examenId, Ejercicio ejercicio);
    void removerEjercicioDeExamen(Integer examenId, Integer ejercicioId);
    void calificarExamen(Integer examenId);

    List<Examen> buscarExamenesPorFecha(LocalDate fecha);
    List<Examen> buscarExamenesPorEstudiante(Integer estudianteId);
    List<Examen> buscarExamenesPorProfesor(Integer profesorId);
    List<Examen> buscarExamenesPorClase(Integer claseId);

    double calcularPromedioExamenes(Integer estudianteId);

}
