package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.RecursoDto;
import com.lautaro.crud.service.RecursosService;
import com.lautaro.entity.mapper.RecursoMapper;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.materia.MateriaRepository;
import com.lautaro.entity.recursos.Formato;
import com.lautaro.entity.recursos.Recurso;
import com.lautaro.entity.recursos.RecursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecursoServiceImpl implements RecursosService {

    private RecursoRepository recursoRepository;
    private MateriaRepository materiaRepository;

    @Value("${upload.dir}") // Ruta de almacenamiento configurada en application.properties
    private String uploadDir;

    @Override
    public Recurso crearRecurso(RecursoDto recursoDto) {
        // Validar que el recurso no exista
        if (validarRecursoUnico(recursoDto)) {
            throw new IllegalArgumentException("El recurso ya existe.");
        }

        // Buscar la materia
        Materia materia = materiaRepository.findById(recursoDto.getMateriaId())
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada."));

        // Guardar archivo en el sistema
        String rutaArchivo = guardarArchivo(recursoDto.getArchivo());

        // Crear la entidad `Recurso`
        Recurso nuevoRecurso = RecursoMapper.toEntity(recursoDto);

        // Guardar en la base de datos
        return recursoRepository.save(nuevoRecurso);
    }


    private String guardarArchivo(MultipartFile archivo) {
        validarTipoArchivo(archivo);
        validarExtensionArchivo(archivo);
        try {
            Path directorioPath = Paths.get(uploadDir);
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }
            String nombreArchivo = archivo.getOriginalFilename();
            Path rutaArchivo = directorioPath.resolve(nombreArchivo);
            archivo.transferTo(rutaArchivo.toFile());
            return rutaArchivo.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo.", e);
        }
    }


    private void validarExtensionArchivo(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null ||
                !(nombreArchivo.endsWith(".jpg") ||
                        nombreArchivo.endsWith(".jpeg") ||
                        nombreArchivo.endsWith(".png") ||
                        nombreArchivo.endsWith(".pdf") ||
                        nombreArchivo.endsWith(".mp4"))) {
            throw new IllegalArgumentException("Extensión de archivo no permitida. Solo se permiten JPG, PNG, PDF y MP4.");
        }
    }


    private void validarTipoArchivo(MultipartFile archivo) {
        String tipoMime = archivo.getContentType();
        if (tipoMime == null ||
                (!tipoMime.startsWith("image/") &&  // Imágenes (JPEG, PNG, etc.)
                        !tipoMime.equals("application/pdf") && // PDF
                        !tipoMime.startsWith("video/"))) { // Videos (MP4, etc.)
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten imágenes, videos y PDFs.");
        }
    }





    @Override
    public void eliminarRecurso(Recurso recurso) {
        if (recursoRepository.existsById(recurso.getId())) {
            recursoRepository.delete(recurso);
        } else {
            throw new IllegalArgumentException("El recurso no existe.");
        }
    }

    @Override
    public Recurso obtenerRecursoPorId(Integer recursoId) {
        return recursoRepository.findById(recursoId)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado."));
    }

    @Override
    public Recurso actualizarRecurso(Integer recursoId, Recurso recurso) {
        Optional<Recurso> recursoExistente = recursoRepository.findById(recursoId);
        if (recursoExistente.isPresent()) {
            recurso.setId(recursoId);
            return recursoRepository.save(recurso);
        }
        throw new IllegalArgumentException("El recurso no existe.");
    }

    @Override
    public List<Recurso> listarRecursosPorMateria(Integer materiaId) {
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada."));
        return materia.getRecursos();
    }

    @Override
    public List<Recurso> buscarRecursosPorNombre(String nombre) {
        return recursoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Recurso> buscarRecursosPorTipo(String tipo) {
        return recursoRepository.findByTipo(tipo);
    }

    @Override
    public Recurso asignarRecursoAMateria(Integer recursoId, Materia materia) {
        Recurso recurso = obtenerRecursoPorId(recursoId);
        materia.agregarRecurso(recurso);
        materiaRepository.save(materia);
        return recurso;
    }

    @Override
    public boolean validarRecursoUnico(RecursoDto recurso) {
        return recursoRepository.existsByNombreAndFormato(recurso.getNombreRecurso(), recurso.getFormato());
    }


    @Override
    public List<Recurso> obtenerRecursosRecientes() {
        return recursoRepository.findTop5ByOrderByFechaCreacionDesc();
    }

    @Override
    public List<Recurso> obtenerRecursosDestacados() {
        return recursoRepository.findTop5ByDestacadoTrue();
    }


}
