package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.EstudianteDto;
import com.lautaro.crud.service.EstudianteService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.ColegioRepository;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.AulaRepository;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.estudiante.EstudianteRepository;
import com.lautaro.entity.user.User;
import com.lautaro.entity.user.UserRepository;
import com.lautaro.exception.aula.AulaNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundException;
import com.lautaro.exception.estudiante.EstudianteNotFoundException;
import com.lautaro.exception.examen.ExamenNotFoundException;
import com.lautaro.exception.user.UserNotFoundException;
import com.lautaro.mapper.EstudianteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UserRepository userRepository;
    private final ColegioRepository colegioRepository;
    private final AulaRepository aulaRepository;

    @Override
    @Transactional
    public Estudiante crearEstudiante(EstudianteDto estudianteDto) {
//        User user = userRepository.findByEmail(estudianteDto.getEmail())
//                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + estudianteDto.getEmail()));
//
//        Colegio colegio = colegioRepository.findByNombre(estudianteDto.getNombreColegio())
//                .orElseThrow(() -> new ColegioNotFoundException("Colegio no encontrado: " + estudianteDto.getNombreColegio()));
//
//        Estudiante nuevoEstudiante = EstudianteMapper.toEntity(estudianteDto);
//        nuevoEstudiante.setUser(user);
//        nuevoEstudiante.setColegio(colegio);
//        user.setPersona(nuevoEstudiante);
//
//        nuevoEstudiante.setPromedio(0.0);
//        nuevoEstudiante.setRankEnAula(null);
//        nuevoEstudiante.setRankEnColegio(null);
//
//        if (estudianteDto.getAulaId() != null) {
//            Aula aula = aulaRepository.findById(estudianteDto.getAulaId())
//                    .orElseThrow(() -> new AulaNotFoundException("Aula no encontrada con ID: " + estudianteDto.getAulaId()));
//            nuevoEstudiante.setAula(aula);
//        }
//
//        estudianteRepository.save(nuevoEstudiante);
//        userRepository.save(user);
//        colegio.getEstudiantes().add(nuevoEstudiante);
//        colegioRepository.save(colegio);
//
//        return nuevoEstudiante;
        return null;
    }

    @Override
    @Transactional
    public void calcularPromedio(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        if (estudiante.getExamenes().isEmpty()) {
            estudiante.setPromedio(0.0);
        } else {
            double sumaNotas = estudiante.getExamenes().stream()
                    .mapToDouble(Examen::getNota)
                    .sum();
            estudiante.setPromedio(sumaNotas / estudiante.getExamenes().size());
        }
        estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public void agregarExamen(Estudiante estudiante, Examen examen) {
        if (estudiante == null || examen == null) {
            throw new IllegalArgumentException("El estudiante y el examen no pueden ser nulos");
        }
        estudiante.getExamenes().add(examen);
        examen.setEstudiante(estudiante);
        calcularPromedio(estudiante);
    }

    @Override
    @Transactional
    public void removerExamen(Estudiante estudiante, Examen examen) {
        if (estudiante == null || examen == null) {
            throw new IllegalArgumentException("El estudiante y el examen no pueden ser nulos");
        }
        if (!estudiante.getExamenes().remove(examen)) {
            throw new ExamenNotFoundException(examen.getId());
        }
        examen.setEstudiante(null);
        calcularPromedio(estudiante);
    }

    @Override
    public Map<String, Double> calcularPromedioPorMateria(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        return estudiante.getExamenes().stream()
                .collect(Collectors.groupingBy(e -> e.getClase().getMateria(),
                        Collectors.averagingDouble(Examen::getNota)));
    }

    @Override
    public List<Estudiante> buscarEstudiantes() {
        return estudianteRepository.findAll();
    }

    @Override
    @Transactional
    public void asignarEstudianteAula(Estudiante estudiante, Aula aula) {
        if (estudiante == null || aula == null) {
            throw new IllegalArgumentException("El estudiante y el aula no pueden ser nulos");
        }
        estudiante.setAula(aula);
        aula.getEstudiantes().add(estudiante);
        estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public void removerEstudianteDeAula(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        if (estudiante.getAula() != null) {
            estudiante.getAula().getEstudiantes().remove(estudiante);
            estudiante.setAula(null);
            estudianteRepository.save(estudiante);
        }
    }

    @Override
    @Transactional
    public void actualizarRanking(Colegio colegio) {
        if (colegio == null) {
            throw new IllegalArgumentException("El colegio no puede ser nulo");
        }
        List<Estudiante> estudiantes = colegio.getEstudiantes();
        estudiantes.sort((e1, e2) -> Double.compare(e2.getPromedio(), e1.getPromedio()));

        // Actualizar ranking en colegio
        for (int i = 0; i < estudiantes.size(); i++) {
            estudiantes.get(i).setRankEnColegio(i + 1);
        }

        // Actualizar ranking por aula
        Map<Aula, List<Estudiante>> estudiantesPorAula = estudiantes.stream()
                .filter(e -> e.getAula() != null)
                .collect(Collectors.groupingBy(Estudiante::getAula));

        estudiantesPorAula.forEach((aula, estudiantesEnAula) -> {
            estudiantesEnAula.sort((e1, e2) -> Double.compare(e2.getPromedio(), e1.getPromedio()));
            for (int i = 0; i < estudiantesEnAula.size(); i++) {
                estudiantesEnAula.get(i).setRankEnAula(i + 1);
            }
        });

        estudianteRepository.saveAll(estudiantes);
    }

    @Override
    public Estudiante buscarEstudiantePorCardId(Long cardId) {
        if (cardId == null) {
            throw new IllegalArgumentException("El cardId no puede ser nulo");
        }
        return estudianteRepository.findByCardId(cardId)
                .orElseThrow(() -> new EstudianteNotFoundException(cardId));
    }

    @Override
    public Map<String, Double> obtenerRendimientoComparativo(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }

        Map<String, Double> promediosEstudiante = calcularPromedioPorMateria(estudiante);
        Map<String, Double> promediosGenerales = estudiante.getAula().getEstudiantes().stream()
                .flatMap(e -> e.getExamenes().stream())
                .collect(Collectors.groupingBy(
                        e -> e.getClase().getMateria(),
                        Collectors.averagingDouble(Examen::getNota)
                ));

        return promediosEstudiante.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() - promediosGenerales.getOrDefault(entry.getKey(), 0.0)
                ));
    }

    @Override
    public List<Estudiante> buscarEstudiantesPorRendimiento(Colegio colegio, double promedioMinimo) {
        if (colegio == null) {
            throw new IllegalArgumentException("El colegio no puede ser nulo");
        }
        if (promedioMinimo < 0) {
            throw new IllegalArgumentException("El promedio mínimo no puede ser negativo");
        }
        return colegio.getEstudiantes().stream()
                .filter(e -> e.getPromedio() >= promedioMinimo)
                .collect(Collectors.toList());
    }
}