package com.lautaro.crud.service.impl;

import com.lautaro.crud.service.ClaseService;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.clase.ClaseRepository;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.colegio.aula.clase.examen.ExamenRepository;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.estudiante.EstudianteRepository;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.persona.profesor.ProfesorRepository;
import com.lautaro.exception.aula.AulaLlenaException;
import com.lautaro.exception.ConflictoHorarioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ClaseServiceImpl implements ClaseService {


    private final ClaseRepository claseRepository;
    private final ProfesorRepository profesorRepository;
    private final ExamenRepository examenRepository;

    @Override
    public Clase crearClase(Clase clase) {
        return claseRepository.save(clase);
    }

    @Override
    public Optional<Clase> obtenerClasePorId(Integer id) {
        return claseRepository.findById(id);
    }

    @Override
    public List<Clase> obtenerTodasLasClases() {
        return claseRepository.findAll();
    }

    @Override
    public Clase actualizarClase(Integer id, Clase claseActualizada) {
        return claseRepository.findById(id)
                .map(clase -> {
                    clase.setMateria(claseActualizada.getMateria());
                    clase.setFechaClase(claseActualizada.getFechaClase());
                    clase.setHoraInicio(claseActualizada.getHoraInicio());
                    clase.setHoraFin(claseActualizada.getHoraFin());
                    return claseRepository.save(clase);
                })
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + id));
    }

    @Override
    public void eliminarClase(Integer id) {
        claseRepository.deleteById(id);
    }



    @Override
    public void asignarProfesorAClase(Integer claseId, Integer profesorId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + profesorId));

        clase.setProfesor(profesor);
        claseRepository.save(clase);
    }

    @Override
    public void agregarExamenAClase(Integer claseId, Examen examen) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));

        clase.addExamen(examen);
        claseRepository.save(clase);
    }

    @Override
    public void removerExamenDeClase(Integer claseId, Integer examenId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado con id: " + examenId));

        clase.removeExamen(examen);
        claseRepository.save(clase);
    }

    @Override
    public List<Clase> buscarClasesPorFecha(LocalDate fecha) {
        return claseRepository.findByFechaClase(fecha);
    }

    @Override
    public List<Clase> buscarClasesPorProfesor(Integer profesorId) {
        return claseRepository.findByProfesorId(profesorId);
    }

    @Override
    public List<Clase> buscarClasesPorAula(Integer aulaId) {
        return claseRepository.findByAulaId(aulaId);
    }

    @Override
    public boolean verificarConflictoHorario(Clase nuevaClase) {
        List<Clase> clasesExistentes = claseRepository.findByFechaClase(nuevaClase.getFechaClase());
        return clasesExistentes.stream()
                .anyMatch(clase -> clase.tieneConflictoHorario(nuevaClase));
    }

}
