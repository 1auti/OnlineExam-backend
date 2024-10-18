package com.lautaro.crud.service.impl;

import com.lautaro.crud.service.ProfesorService;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.clase.ClaseRepository;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.persona.profesor.ProfesorRepository;
import com.lautaro.exception.clase.ClaseNotFoundException;
import com.lautaro.exception.profesor.ProfesorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final ClaseRepository claseRepository;


    @Override
    public Profesor crearProfesor(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor buscarProfesorPorId(Integer id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException(id));
    }

    @Override
    public List<Profesor> buscarTodosProfesores() {
        return profesorRepository.findAll();
    }

    @Override
    public Profesor actualizarProfesor(Profesor profesor) {
        if (!profesorRepository.existsById(profesor.getId())) {
            throw new ProfesorNotFoundException(profesor.getId());
        }
        return profesorRepository.save(profesor);
    }

    @Override
    public void eliminarProfesor(Integer id) {
        if (!profesorRepository.existsById(id)) {
            throw new ProfesorNotFoundException(id);
        }
        profesorRepository.deleteById(id);
    }

    @Override
    public void asignarClase(Integer profesorId, Integer claseId) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ClaseNotFoundException(claseId));

        profesor.getClases().add(clase);
        clase.setProfesor(profesor);
        profesorRepository.save(profesor);
    }

    @Override
    public void removerClase(Integer profesorId, Integer claseId) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ClaseNotFoundException(claseId));

        profesor.getClases().remove(clase);
        clase.setProfesor(null);
        profesorRepository.save(profesor);
    }

    @Override
    public List<Clase> buscarClasesDeProfesor(Integer profesorId) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        return profesor.getClases();
    }

    @Override
    public int calcularCargaHoraria(Integer profesorId) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        return profesor.getClases().stream()
                .mapToInt(c -> (int) ChronoUnit.HOURS.between(c.getHoraInicio(), c.getHoraFin()))
                .sum();
    }

    @Override
    public Map<String, Integer> calcularCargaHorariaPorMateria(Integer profesorId) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        return profesor.getClases().stream()
                .collect(Collectors.groupingBy(
                        Clase::getMateria,
                        Collectors.summingInt(c -> (int) ChronoUnit.HOURS.between(c.getHoraInicio(), c.getHoraFin()))
                ));
    }

    @Override
    public boolean verificarDisponibilidad(Integer profesorId, LocalDateTime inicio, LocalDateTime fin) {
        Profesor profesor = buscarProfesorPorId(profesorId);
        return profesor.getClases().stream()
                .noneMatch(c -> (c.getHoraInicio().isBefore(fin) && c.getHoraFin().isAfter(inicio)));
    }

    @Override
    public List<Profesor> buscarProfesoresPorMateria(String materia) {
        return profesorRepository.findAll().stream()
                .filter(p -> p.getClases().stream().anyMatch(c -> c.getMateria().equals(materia)))
                .collect(Collectors.toList());
    }
}
