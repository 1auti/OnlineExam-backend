package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.ClaseDto;
import com.lautaro.crud.service.ClaseService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.ColegioRepository;
import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.aula.AulaRepository;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.clase.ClaseRepository;
import com.lautaro.entity.examen.Examen;
import com.lautaro.entity.examen.ExamenRepository;
import com.lautaro.entity.mapper.ClaseMapper;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.persona.profesor.ProfesorRepository;
import com.lautaro.exception.ConflictoHorarioException;
import com.lautaro.exception.aula.AulaNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundNombreException;
import com.lautaro.exception.profesor.ProfesorNotFoundException;
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
    private final AulaRepository aulaRepository;
    private final ColegioRepository colegioRepository;

    @Override
    public Clase crearClase(ClaseDto clase) {
        Colegio colegio = colegioRepository.findByNombre(clase.getAulaDto().getNombreColegio())
                .orElseThrow(() -> new ColegioNotFoundNombreException(clase.getAulaDto().getNombreColegio()));

        Aula aula = aulaRepository.findByColegioIdAndAnioAndGradoAndModalidad(colegio.getId(),clase.getAulaDto().getAnio(),
                clase.getAulaDto().getGrado(),clase.getAulaDto().getModalidad())
                .orElseThrow(() -> new AulaNotFoundException("El aula no fue encontrada"));

        Profesor profesor = profesorRepository.findByEmail(clase.getEmailProfesor())
                .orElseThrow( () -> new ProfesorNotFoundException("El email no fue encontrado"));


        Clase nuevaClase = ClaseMapper.toEntity(clase);
        nuevaClase.setAula(aula);
        nuevaClase.setProfesor(profesor);

        // Verificar conflictos de horario
        if (verificarConflictoHorario(nuevaClase)) {
            throw new ConflictoHorarioException("La clase no puede ser creada debido a un conflicto de horario en el aula");
        }

        //guardamos si no hay conflicto
        Clase claseGuardada = claseRepository.save(nuevaClase);

        return claseGuardada;

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
        clase = claseRepository.save(clase);
    }

    @Override
    public void eliminarProfesorAClase(Integer claseId,Integer profesorId){

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no fue encontrada: " + claseId));
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow( () -> new RuntimeException("Profesor no fue encontrada" + profesorId));

        clase.setProfesor(null);
        clase = claseRepository.save(clase);
    }

    @Override
    public void agregarExamenAClase(Integer claseId, Examen examen) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));

        clase.setExamen(examen);
        claseRepository.save(clase);
    }

    @Override
    public void removerExamenDeClase(Integer claseId, Integer examenId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + claseId));
        Examen examen = examenRepository.findById(examenId)
                .orElseThrow(() -> new RuntimeException("Examen no encontrado con id: " + examenId));

        clase.setExamen(null);
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
        List<Clase> clasesConConflicto = claseRepository.findClasesConConflictoHorarioPorAula(
                nuevaClase.getAula().getId(),
                nuevaClase.getFechaClase(),
                nuevaClase.getHoraInicio(),
                nuevaClase.getHoraFin()
        );
        return !clasesConConflicto.isEmpty();
    }

    @Override
    public List<Clase> traerClasesPorMateria(Materia materia) {
        return claseRepository.findAllByMateria(materia);
    }


}
