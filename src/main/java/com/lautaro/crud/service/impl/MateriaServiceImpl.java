package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.MateriaDto;
import com.lautaro.crud.service.MateriaService;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.mapper.MateriaMapper;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.materia.MateriaRepository;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.estudiante.EstudianteRepository;
import com.lautaro.entity.recursos.Recurso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MateriaServiceImpl implements MateriaService {

    private MateriaRepository materiaRepository;
    private EstudianteRepository estudianteRepository;

    @Override
    public Materia crearMateria(MateriaDto materiaDto) {
        // Validar que el nombre de la materia no sea vacío o nulo
        if (materiaDto.getNombreMateria() == null || materiaDto.getNombreMateria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
        }

        // Validar si la materia ya existe en el colegio
        boolean existeMateria = materiaRepository.existsByNombreMateriaAndColegioId(
                materiaDto.getNombreMateria(),
                materiaDto.getColegio().getId()
        );

        if (existeMateria) {
            throw new IllegalStateException("La materia ya existe en este colegio");
        }

        // Crear la nueva entidad Materia a partir del DTO
        Materia nuevaMateria = MateriaMapper.toEntity(materiaDto);

        // Otras validaciones personalizadas, si es necesario
        if (materiaDto.getRecursos() != null && materiaDto.getRecursos().isEmpty()) {
            throw new IllegalArgumentException("La materia debe tener al menos un recurso asociado");
        }

        // Guardar la nueva materia en la base de datos
        return materiaRepository.save(nuevaMateria);
    }


    @Override
    public void eliminarMateria(Integer materiaId) {
      materiaRepository.deleteById(materiaId);
    }

    @Override
    public Materia buscarMateria(String nombreMateria, Clase clase) {
        return materiaRepository.findBynombreMateriaAndClase(nombreMateria,clase);
    }

    @Override
    public Materia editarMateria(Materia materia) {
        // Validar que el objeto materia no sea nulo
        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser nula");
        }

        // Validar que el ID de la materia sea válido
        if (materia.getId() == null) {
            throw new IllegalArgumentException("El ID de la materia es requerido para editarla");
        }

        // Buscar la materia existente en la base de datos
        Materia materiaExistente = materiaRepository.findById(materia.getId())
                .orElseThrow(() -> new IllegalArgumentException("La materia con el ID especificado no existe"));

        // Validar que el nombre no sea vacío
        if (materia.getNombreMateria() == null || materia.getNombreMateria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
        }

        // Verificar que no exista otra materia con el mismo nombre en el mismo colegio
        boolean existeMateriaDuplicada = materiaRepository.existsByNombreMateriaAndColegioId(
                materia.getNombreMateria(),
                materiaExistente.getColegio().getId()
        );

        if (existeMateriaDuplicada && !materia.getNombreMateria().equalsIgnoreCase(materiaExistente.getNombreMateria())) {
            throw new IllegalStateException("Ya existe otra materia con el mismo nombre en este colegio");
        }

        // Actualizar los campos de la materia existente
        materiaExistente.setNombreMateria(materia.getNombreMateria());
        materiaExistente.setProfesor(materia.getProfesor()); // Si deseas cambiar al profesor
        materiaExistente.setRecursos(materia.getRecursos()); // Si deseas actualizar los recursos

        // Guardar los cambios en la base de datos
        return materiaRepository.save(materiaExistente);
    }


    @Override
    public Estudiante asignarEstudiante(Estudiante estudiante, Materia materia) {
        // Validar que los objetos estudiante y materia no sean nulos
        if (estudiante == null || materia == null) {
            throw new IllegalArgumentException("Estudiante y Materia no pueden ser nulos");
        }

        // Verificar si el estudiante ya aprobó la materia
        if (materiaRepository.isMateriaAprobadaByEstudiante(estudiante.getId(), materia.getId())) {
            throw new IllegalStateException("El estudiante ya aprobó esta materia y no puede ser asignado nuevamente");
        }

        // Asignar la materia al estudiante si no está ya asignada
        if (!estudiante.getMaterias().contains(materia)) {
            estudiante.getMaterias().add(materia);
            materia.getEstudiantes().add(estudiante); // Agregar el estudiante a la lista de la materia
        } else {
            throw new IllegalStateException("El estudiante ya está asignado a esta materia");
        }

        // Guardar los cambios en el repositorio
        estudianteRepository.save(estudiante); // Si tienes un repositorio de estudiantes
        materiaRepository.save(materia);

        return estudiante;
    }


    @Override
    public void removerEstudiante(Integer estudianteId, Materia materia) {
        // Validar que los argumentos no sean nulos
        if (estudianteId == null || materia == null) {
            throw new IllegalArgumentException("El ID del estudiante y la materia no pueden ser nulos");
        }

        // Buscar el estudiante en la lista de estudiantes de la materia
        Estudiante estudiante = materia.getEstudiantes()
                .stream()
                .filter(e -> e.getId().equals(estudianteId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El estudiante con ID " + estudianteId + " no está asignado a esta materia"));

        // Remover la relación estudiante-materia
        materia.getEstudiantes().remove(estudiante);
        estudiante.getMaterias().remove(materia);

        // Guardar los cambios en la base de datos
        materiaRepository.save(materia);
        estudianteRepository.save(estudiante); // Si tienes un repositorio para estudiantes
    }


    @Override
    public List<Recurso> traerRecursos(Integer materiaId) {
        return materiaRepository.findByRecursos_Id(materiaId);
    }

    @Override
    public List<Materia> traerExamenes(Integer materiaId) {
        return materiaRepository.findByExamenes_Id(materiaId);
    }
}
