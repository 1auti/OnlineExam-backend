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
import com.lautaro.exception.aula.AulaLlenaException;
import com.lautaro.exception.aula.AulaNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundException;
import com.lautaro.exception.colegio.ColegioNotFoundNombreException;
import com.lautaro.exception.estudiante.EstudianteNotFoundException;
import com.lautaro.exception.examen.ExamenNotFoundException;
import com.lautaro.exception.user.UserNotFoundException;
import com.lautaro.entity.mapper.EstudianteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final UserRepository userRepository;
    private final ColegioRepository colegioRepository;
    private final AulaRepository aulaRepository;

    private static final double PROMEDIO_INICIAL = 0.0;
    private static final Integer RANK_INICIAL = null;

    @Override
    @Transactional
    public Estudiante crearEstudiante(EstudianteDto estudianteDto) {
        User user = userRepository.findByEmail(estudianteDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + estudianteDto.getEmail()));

        Colegio colegio = colegioRepository.findByNombre(estudianteDto.getAulaDto().getNombreColegio())
                .orElseThrow(() -> new ColegioNotFoundNombreException("Colegio no encontrado: " + estudianteDto.getAulaDto().getNombreColegio()));


        Aula aula = aulaRepository.findByColegioIdAndAnioAndGradoAndModalidad(colegio.getId(), estudianteDto.getAulaDto().getAnio(),
                        estudianteDto.getAulaDto().getGrado(), estudianteDto.getAulaDto().getModalidad())
                .orElseThrow(() -> new AulaNotFoundException("Aula no encontrada"));


        Estudiante nuevoEstudiante = EstudianteMapper.toEntity(estudianteDto);
        nuevoEstudiante.setUser(user);
        nuevoEstudiante.setColegio(colegio);
        user.setPersona(nuevoEstudiante);
        nuevoEstudiante.setAula(aula);
        aula.getEstudiantes().add(nuevoEstudiante);
        colegio.getEstudiantes().add(nuevoEstudiante);
        nuevoEstudiante.setPromedio(PROMEDIO_INICIAL);
        nuevoEstudiante.setRankEnAula(RANK_INICIAL);
        nuevoEstudiante.setRankEnColegio(RANK_INICIAL);
        nuevoEstudiante.setCardId(generarCardId(nuevoEstudiante));

        Estudiante estudianteGuardado = estudianteRepository.save(nuevoEstudiante);
        userRepository.save(user);
        aulaRepository.save(aula);
        colegioRepository.save(colegio);

        return estudianteGuardado;

    }

    @Override
    public boolean eliminarEstudiante(Integer id) {
        // Verificar si el estudiante existe antes de eliminarlo
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);  // Eliminar el estudiante si existe
            return true;
        } else {
            return false;  // No se encontró el estudiante con ese ID
        }
    }

    @Override
    @Transactional
    public Estudiante actualizarEstudiante(Long cardId, EstudianteDto estudianteDto) {

        Colegio colegio = colegioRepository.findByNombre(estudianteDto.getAulaDto().getNombreColegio())
                .orElseThrow( () -> new ColegioNotFoundNombreException(estudianteDto.getNombre()));

        Estudiante estudiante = estudianteRepository.findByCardIdAndColegioId(cardId,colegio.getId())
                .orElseThrow(() -> new EstudianteNotFoundException(cardId));

        // Actualizar campos básicos de Persona
        estudiante.setNombre(estudianteDto.getNombre());
        estudiante.setApellido(estudianteDto.getApellido());
        estudiante.setTel(estudianteDto.getTel());
        estudiante.setSexo(estudianteDto.getSexo());
        estudiante.setEdad(estudianteDto.getEdad());
        estudiante.setEmail(estudianteDto.getEmail());

        // Actualizar campos específicos de Estudiante
        if (estudianteDto.getAulaDto() != null) {
            Aula nuevaAula = aulaRepository.findByColegioIdAndAnioAndGradoAndModalidad(
                            estudiante.getColegio().getId(),
                            estudianteDto.getAulaDto().getAnio(),
                            estudianteDto.getAulaDto().getGrado(),
                            estudianteDto.getAulaDto().getModalidad())
                    .orElseThrow(() -> new AulaNotFoundException("Aula no encontrada"));

            if(!Objects.equals(nuevaAula.getId(), estudiante.getAula().getId())){

                if (!nuevaAula.estaLlena()){
                    // Remover estudiante del aula anterior si existe
                    if (estudiante.getAula() != null) {
                        estudiante.getAula().getEstudiantes().remove(estudiante);
                    }

                    estudiante.setAula(nuevaAula);
                    nuevaAula.getEstudiantes().add(estudiante);
                    nuevaAula.calcularRankingEstudiantes();
                    nuevaAula.calcularPromedioAula();
                }else{
                    throw new AulaLlenaException("El aula está llena y no puede aceptar más estudiantes.");
                }
            }

        }


        // No actualizamos directamente el promedio, rankEnAula y rankEnColegio
        // ya que estos se calculan basados en los exámenes y la posición relativa

        // Actualizar el colegio solo si ha cambiado
        if (estudianteDto.getAulaDto().getNombreColegio() != null && !Objects.equals(estudiante.getColegio().getNombre(), estudianteDto.getAulaDto().getNombreColegio())) {
            Colegio nuevoColegio = colegioRepository.findByNombre(estudianteDto.getAulaDto().getNombreColegio())
                    .orElseThrow(() -> new ColegioNotFoundNombreException(estudianteDto.getAulaDto().getNombreColegio()));

            estudiante.getColegio().getEstudiantes().remove(estudiante);
            estudiante.setColegio(nuevoColegio);
            nuevoColegio.getEstudiantes().add(estudiante);
        }

        // No actualizamos los exámenes aquí, eso debería manejarse en un método separado

        log.info("Estudiante actualizado con cardId: {}", cardId);
        return estudianteRepository.save(estudiante);
    }


    private Long generarCardId(Estudiante nuevoEstudiante) {
        Random random = new Random();
        Long cardId;
        Integer colegioId = nuevoEstudiante.getColegio().getId();

        // Generar un cardId y verificar que no exista para el colegio dado
        do {
            cardId = random.nextLong(); // Genera un número random tipo Long
        } while (estudianteRepository.findByCardIdAndColegioId(cardId, colegioId).isPresent());

        return cardId;
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
        estudiante.agregarExamen(examen);
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
        removerExamen(estudiante,examen);
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
        } else if (aula.estaLlena()) {
            throw new AulaLlenaException("El aula esta llena no se puede agregar a otro estudiante");
        }

        estudiante.setAula(aula);
        aula.getEstudiantes().add(estudiante);
        estudiante = estudianteRepository.save(estudiante);
        aula.calcularPromedioAula();
        aula.calcularRankingEstudiantes();
        aula = aulaRepository.save(aula);
    }

    @Override
    @Transactional
    public void removerEstudianteDeAula(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        if (estudiante.getAula() != null) {
            Aula aula = estudiante.getAula();
            estudiante.getAula().getEstudiantes().remove(estudiante);
            estudiante.setAula(null);
            aula.calcularPromedioAula();
            aula.calcularRankingEstudiantes();
            aula = aulaRepository.save(aula);
            estudianteRepository.save(estudiante);

        }
    }

    @Override
    @Transactional
    public void actualizarRanking(Colegio colegio) {
        if (colegio == null) {
            throw new IllegalArgumentException("El colegio no puede ser nulo");
        }

        // Obtener y ordenar los estudiantes por promedio
        List<Estudiante> estudiantes = colegio.getEstudiantes();
        estudiantes.sort((e1, e2) -> Double.compare(e2.getPromedio(), e1.getPromedio()));

        // Actualizar ranking en colegio
        for (int i = 0; i < estudiantes.size(); i++) {
            estudiantes.get(i).setRankEnColegio(i + 1);
        }

        // Agrupar estudiantes por aula y actualizar ranking dentro de cada aula
        Map<Aula, List<Estudiante>> estudiantesPorAula = estudiantes.stream()
                .filter(e -> e.getAula() != null)
                .collect(Collectors.groupingBy(Estudiante::getAula));

        estudiantesPorAula.forEach((aula, estudiantesEnAula) -> {
            estudiantesEnAula.sort((e1, e2) -> Double.compare(e2.getPromedio(), e1.getPromedio()));
            for (int i = 0; i < estudiantesEnAula.size(); i++) {
                estudiantesEnAula.get(i).setRankEnAula(i + 1);
            }
            // Guardar cambios en cada aula después de actualizar su ranking
            aulaRepository.save(aula);
        });

        // Guardar los cambios en los estudiantes
        estudianteRepository.saveAll(estudiantes);

        // Guardar los cambios en el colegio después de actualizar el ranking general
        colegioRepository.save(colegio);
    }


    @Override
    public Estudiante buscarEstudiantePorCardId(Long cardId, Integer colegioId) {
        if (cardId == null) {
            throw new IllegalArgumentException("El cardId no puede ser nulo");
        }
        return estudianteRepository.findByCardIdAndColegioId(cardId,colegioId)
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