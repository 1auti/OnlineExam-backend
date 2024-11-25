package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.ProfesorDto;
import com.lautaro.crud.service.ProfesorService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.ColegioRepository;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.clase.ClaseRepository;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.persona.profesor.ProfesorRepository;
import com.lautaro.entity.user.User;
import com.lautaro.entity.user.UserRepository;
import com.lautaro.exception.clase.ClaseNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundNombreException;
import com.lautaro.exception.profesor.ProfesorNotFoundException;
import com.lautaro.exception.user.UserNotFoundException;
import com.lautaro.entity.mapper.ProfesorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
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
    private final UserRepository userRepository;
    private final ColegioRepository colegioRepository;



    @Override
    public Profesor crearProfesor(ProfesorDto profesor) {

        User user = userRepository.findByEmail(profesor.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + profesor.getEmail()));

        Colegio colegio = colegioRepository.findByNombre(profesor.getNombreColegio())
                .orElseThrow(()-> new ColegioNotFoundNombreException("Colegio no fue encontrado" + profesor.getNombreColegio()));

        Profesor nuevoProfesor = ProfesorMapper.toEntity(profesor);
        nuevoProfesor.setUser(user);
        nuevoProfesor.setColegio(colegio);
        colegio.getProfesores().add(nuevoProfesor);

        Profesor profesorGuardado = profesorRepository.save(nuevoProfesor);
        user = userRepository.save(user);
        colegio = colegioRepository.save(colegio);

        return profesorGuardado;

    }

    @Override
    public Profesor buscarProfesorPorId(Integer id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException("El profesor no fue encontrado" + id));
    }

    @Override
    public List<Profesor> buscarTodosProfesores() {
        return profesorRepository.findAll();
    }

    @Override
    public Profesor actualizarProfesor(Profesor profesor) {
        if (!profesorRepository.existsById(profesor.getId())) {
            throw new ProfesorNotFoundException("El profesor no fue encontrado" + profesor.getId());
        }
        return profesorRepository.save(profesor);
    }

    @Override
    public List<Profesor> trarProfesoresPorColegio(Colegio colegio) {
        return profesorRepository.findByColegioId(colegio.getId());
    }

    @Override
    public void eliminarProfesor(Integer id) {
        if (!profesorRepository.existsById(id)) {
            throw new ProfesorNotFoundException("El profesor no fue encontrado" + id);
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

        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no existe.");
        }

        // Agrupar por materia y sumar las horas de las clases.
        return profesor.getClases().stream()
                .collect(Collectors.groupingBy(
                        clase -> clase.getMateria().getNombreMateria(), // Agrupa por nombre de la materia.
                        Collectors.summingInt(clase ->
                                (int) ChronoUnit.HOURS.between(clase.getHoraInicio(), clase.getHoraFin()) // Calcula las horas.
                        )
                ));
    }


    @Override
    public boolean verificarDisponibilidad(Integer profesorId, LocalTime inicio, LocalTime fin) {
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
