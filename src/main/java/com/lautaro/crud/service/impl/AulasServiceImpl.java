package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.AulaDto;
import com.lautaro.crud.service.AulasService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.ColegioRepository;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.AulaRepository;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import com.lautaro.entity.mapper.AulaMapper;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.exception.aula.AulaVaciaException;
import com.lautaro.exception.colegio.ColegioNotFoundNombreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AulasServiceImpl implements AulasService {

    private final AulaRepository aulaRepository;
    private final ColegioRepository colegioRepository;

    private static final Double PROMEDIO_INICIAL = 0.00;
    private static final Integer RANK_INICIAL = null;

    @Override
    public Aula crearAula(AulaDto aula) {

        Colegio colegio = colegioRepository.findByNombre(aula.getNombreColegio())
                .orElseThrow(() -> new ColegioNotFoundNombreException(aula.getNombreColegio()));

        Aula nuevaAula = AulaMapper.toEntity(aula);
        nuevaAula.setRank(RANK_INICIAL);
        nuevaAula.setPromedioClases(PROMEDIO_INICIAL);
        nuevaAula.setColegio(colegio);
        colegio.getAulas().add(nuevaAula);

        Aula aulaGuardada = aulaRepository.save(nuevaAula);
        colegioRepository.save(colegio);

        return aulaGuardada;
    }

    @Override
    public Optional<Aula> obtenerAulaPorId(Integer id) {
        return aulaRepository.findById(id);
    }

    @Override
    public List<Aula> obtenerTodasLasAulas() {
        return aulaRepository.findAll();
    }

    @Override
    public Aula actualizarAula(Integer id, Aula aulaActualizada) {
        return aulaRepository.findById(id)
                .map(aula -> {
                    aula.setGrado(aulaActualizada.getGrado());
                    aula.setModalidad(aulaActualizada.getModalidad());
                    aula.setCapacidadMaxima(aulaActualizada.getCapacidadMaxima());
                    aula.setAnio(aulaActualizada.getAnio());
                    return aulaRepository.save(aula);
                })
                .orElseThrow(() -> new RuntimeException("Aula no encontrada con id: " + id));
    }

    @Override
    public void eliminarAula(Integer id) {
        aulaRepository.deleteById(id);
    }

    @Override
    public List<Aula> buscarAulasPorGradoYModalidad(Grado grado, Modalidad modalidad) {
        return aulaRepository.findByGradoAndModalidad(grado, modalidad);
    }

    @Override
    public void calcularPromedioClases(Integer aulaId) {
        aulaRepository.findById(aulaId).ifPresent(aula -> {
            aula.calcularPromedioAula();
            aulaRepository.save(aula);
        });
    }

    @Override
    public void calcularRankingEstudiantes(Integer aulaId) {
        aulaRepository.findById(aulaId).ifPresent(aula -> {
            aula.calcularRankingEstudiantes();
            aulaRepository.save(aula);
        });
    }

    @Override
    public boolean verificarCapacidad(Integer aulaId) {
        return aulaRepository.findById(aulaId)
                .map(Aula::estaLlena)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada con id: " + aulaId));
    }

    @Override
    public List<Aula> obtenerAulasPorColegio(Integer colegioId) {
        return aulaRepository.findByColegioId(colegioId);
    }

    @Override
    public List<Estudiante> buscarEstudiantePorAula(Aula aula) {
        if (aula == null) {
            throw new IllegalArgumentException("El aula no puede ser nula");
        }

        List<Estudiante> estudiantes = aula.getEstudiantes();

        if (estudiantes.isEmpty()) {
            throw new AulaVaciaException("El aula no puede estar vacía");
        }

        return estudiantes.stream()
                .filter(estudiante ->
                        estudiante.getAula().getAnio().equals(aula.getAnio()) &&
                                estudiante.getAula().getGrado() == aula.getGrado() &&
                                estudiante.getAula().getModalidad() == aula.getModalidad() &&
                                estudiante.getAula().getColegio().equals(aula.getColegio())
                )
                .collect(Collectors.toList());
    }

}
