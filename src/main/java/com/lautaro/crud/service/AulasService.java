package com.lautaro.crud.service;

import com.lautaro.crud.dto.AulaDto;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import com.lautaro.entity.persona.estudiante.Estudiante;

import java.util.List;
import java.util.Optional;

public interface AulasService {

    Aula crearAula(AulaDto aula);
    Optional<Aula> obtenerAulaPorId(Integer id);
    List<Aula> obtenerTodasLasAulas();
    Aula actualizarAula(Integer id, Aula aulaActualizada);
    void eliminarAula(Integer id);

    List<Aula> buscarAulasPorGradoYModalidad(Grado grado, Modalidad modalidad);
    void calcularPromedioClases(Integer aulaId);
    void calcularRankingEstudiantes(Integer aulaId);
    boolean verificarCapacidad(Integer aulaId);
    List<Aula> obtenerAulasPorColegio(Integer colegioId);
    List<Estudiante> buscarEstudiantePorAula(Aula aula);
}
