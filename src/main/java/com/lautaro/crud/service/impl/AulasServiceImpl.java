package com.lautaro.crud.service.impl;

import com.lautaro.crud.service.AulasService;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.AulaRepository;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AulasServiceImpl implements AulasService {

    private final AulaRepository aulaRepository;


    @Override
    public Aula crearAula(Aula aula) {
        return aulaRepository.save(aula);
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
}
