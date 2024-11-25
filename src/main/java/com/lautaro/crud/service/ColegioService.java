package com.lautaro.crud.service;

import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;

import java.util.List;

public interface ColegioService {

    Colegio crearColegio(Colegio colegio);
    Colegio buscarColegioPorId(Integer id);
    List<Colegio> buscarTodosColegios();
    Colegio actualizarColegio(Colegio colegio);
    void eliminarColegio(Integer id);

    void agregarProfesor(Integer colegioId, Profesor profesor);
    void removerProfesor(Integer colegioId, Integer profesorId);

    void agregarEstudiante(Integer colegioId, Estudiante estudiante);
    void removerEstudiante(Integer colegioId, Long estudianteCardId);

    void agregarAula(Integer colegioId, Integer aulaId);
    void removerAula(Integer colegioId, Integer aulaId);

    void actualizarEstadisticas(Integer colegioId);
    void actualizarPromedioEstudiante(Integer colegioId, Long estudianteCardId, double promedioAnterior, double promedioNuevo);

    List<Estudiante> obtenerRankingEstudiantes(Integer colegioId);
    List<Aula> obtenerRankingAulas(Integer colegioId);

}
