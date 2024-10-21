package com.lautaro.crud.service.impl;

import com.lautaro.crud.service.ColegioService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.ColegioRepository;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.AulaRepository;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.estudiante.EstudianteRepository;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.persona.profesor.ProfesorRepository;
import com.lautaro.exception.aula.AulaNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundException;
import com.lautaro.exception.estudiante.EstudianteNotFoundException;
import com.lautaro.exception.profesor.ProfesorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ColegioServiceImpl implements ColegioService {

    private ColegioRepository colegioRepository;
    private ProfesorRepository profesorRepository;
    private EstudianteRepository estudianteRepository;
    private AulaRepository aulaRepository;


    @Override
    public Colegio crearColegio(Colegio colegio) {
        return colegioRepository.save(colegio);
    }

    @Override
    public Colegio buscarColegioPorId(Integer id) {
        return colegioRepository.findById(id)
                .orElseThrow(() -> new ColegioNotFoundException(id));
    }

    @Override
    public List<Colegio> buscarTodosColegios() {
        return colegioRepository.findAll();
    }

    @Override
    public Colegio actualizarColegio(Colegio colegio) {
        if (!colegioRepository.existsById(colegio.getId())) {
            throw new ColegioNotFoundException(colegio.getId());
        }
        return colegioRepository.save(colegio);
    }

    @Override
    public void eliminarColegio(Integer id) {
        if (!colegioRepository.existsById(id)) {
            throw new ColegioNotFoundException(id);
        }
        colegioRepository.deleteById(id);
    }

    @Override
    public void agregarProfesor(Integer colegioId, Profesor profesor) {
        Colegio colegio = buscarColegioPorId(colegioId);
        colegio.addProfesor(profesor);
        colegioRepository.save(colegio);
    }

    @Override
    public void removerProfesor(Integer colegioId, Integer profesorId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new ProfesorNotFoundException(profesorId));
        colegio.removeProfesor(profesor);
        colegioRepository.save(colegio);
    }

    @Override
    public void agregarEstudiante(Integer colegioId, Estudiante estudiante) {
        Colegio colegio = buscarColegioPorId(colegioId);
        colegio.getEstudiantes().add(estudiante);
        estudiante.setColegio(colegio);
        colegio.agregarEstudiante(estudiante.getPromedio());
        colegioRepository.save(colegio);
    }

    @Override
    public void removerEstudiante(Integer colegioId, Long estudianteCardId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        Estudiante estudiante = estudianteRepository.findByCardIdAndColegioId(estudianteCardId,colegioId)
                .orElseThrow(() -> new EstudianteNotFoundException(estudianteCardId));
        colegio.getEstudiantes().remove(estudiante);
        estudiante.setColegio(null);
        colegio.eliminarEstudiante(estudiante.getPromedio());
        colegioRepository.save(colegio);
    }

    @Override
    public void agregarAula(Integer colegioId, Aula aula) {
        Colegio colegio = buscarColegioPorId(colegioId);
        colegio.getAulas().add(aula);
        aula.setColegio(colegio);
        colegioRepository.save(colegio);
    }

    @Override
    public void removerAula(Integer colegioId, Integer aulaId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(AulaNotFoundException::new);
        colegio.getAulas().remove(aula);
        aula.setColegio(null);
        colegioRepository.save(colegio);
    }

    @Override
    public void actualizarEstadisticas(Integer colegioId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        colegio.actualizarEstadisticas();
        colegioRepository.save(colegio);
    }

    @Override
    public void actualizarPromedioEstudiante(Integer colegioId, Long estudianteCardId, double promedioAnterior, double promedioNuevo) {
        Colegio colegio = buscarColegioPorId(colegioId);
        Estudiante estudiante = estudianteRepository.findByCardIdAndColegioId(estudianteCardId,colegioId)
                .orElseThrow(() -> new EstudianteNotFoundException(estudianteCardId));
        colegio.actualizarPromedio(promedioAnterior, promedioNuevo);
        estudiante.setPromedio(promedioNuevo);
        colegioRepository.save(colegio);
        estudianteRepository.save(estudiante);
    }

    @Override
    public List<Estudiante> obtenerRankingEstudiantes(Integer colegioId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        List<Estudiante> estudiantes = colegio.getEstudiantes();
        estudiantes.sort(Comparator.comparing(Estudiante::getPromedio).reversed());
        return estudiantes;
    }

    @Override
    public List<Aula> obtenerRankingAulas(Integer colegioId) {
        Colegio colegio = buscarColegioPorId(colegioId);
        List<Aula> aulas = colegio.getAulas();
        aulas.sort(Comparator.comparing(Aula::getPromedioClases).reversed());
        return aulas;
    }
}
