package com.lautaro.crud.service.impl;

import com.lautaro.crud.service.ExamenService;
import com.lautaro.entity.colegio.aula.clase.examen.Ejercicio;
import com.lautaro.entity.colegio.aula.clase.examen.EjercicioRepository;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.colegio.aula.clase.examen.ExamenRepository;
import com.lautaro.exception.examen.ExamenAlreadyGradedException;
import com.lautaro.exception.examen.ExamenIncompleteException;
import com.lautaro.exception.examen.ExamenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamenServiceImpl implements ExamenService {

    private final ExamenRepository examenRepository;
    private final EjercicioRepository ejercicioRepository;

    public Examen crearExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public Examen obtenerExamenPorId(Integer id) {
        return examenRepository.findById(id)
                .orElseThrow(() -> new ExamenNotFoundException(id));
    }

    @Override
    public List<Examen> obtenerTodosLosExamenes() {
        return examenRepository.findAll();
    }

    @Override
    public Examen actualizarExamen(Integer id, Examen examenActualizado) {
        Examen examen = obtenerExamenPorId(id);
        examen.setDificultad(examenActualizado.getDificultad());
        examen.setFechaExamen(examenActualizado.getFechaExamen());
        return examenRepository.save(examen);
    }

    @Override
    public void eliminarExamen(Integer id) {
        examenRepository.deleteById(id);
    }

    @Override
    public void agregarEjercicioAExamen(Integer examenId, Ejercicio ejercicio) {
        Examen examen = obtenerExamenPorId(examenId);
        examen.agregarEjercicio(ejercicio);
        examenRepository.save(examen);
    }

    @Override
    public void removerEjercicioDeExamen(Integer examenId, Integer ejercicioId) {
        Examen examen = obtenerExamenPorId(examenId);
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + ejercicioId));
        examen.removerEjercicio(ejercicio);
        examenRepository.save(examen);
    }

    @Override
    public void calificarExamen(Integer examenId) {
        Examen examen = obtenerExamenPorId(examenId);
        if (!examen.estaTotalmenteCalificado()) {
            throw new ExamenIncompleteException("No se puede calificar un examen incompleto");
        }
        if (examen.getNota() != null) {
            throw new ExamenAlreadyGradedException("El examen ya ha sido calificado");
        }
        examen.calcularNota();
        examenRepository.save(examen);
    }

    @Override
    public List<Examen> buscarExamenesPorFecha(LocalDate fecha) {
        return examenRepository.findByFechaExamen(fecha);
    }

    @Override
    public List<Examen> buscarExamenesPorEstudiante(Integer estudianteId) {
        return examenRepository.findByEstudianteId(estudianteId);
    }

    @Override
    public List<Examen> buscarExamenesPorProfesor(Integer profesorId) {
        return examenRepository.findByProfesorId(profesorId);
    }

    @Override
    public List<Examen> buscarExamenesPorClase(Integer claseId) {
        return examenRepository.findByClaseId(claseId);
    }

    @Override
    public double calcularPromedioExamenes(Integer estudianteId) {
        List<Examen> examenes = buscarExamenesPorEstudiante(estudianteId);
        if (examenes.isEmpty()) {
            return 0.0;
        }
        return examenes.stream()
                .mapToDouble(Examen::getNota)
                .average()
                .orElse(0.0);
    }

}
