package com.lautaro.crud.service;

import com.lautaro.crud.dto.RecursoDto;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.recursos.Recurso;
import org.hibernate.query.Page;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.util.List;

public interface RecursosService {
    Recurso crearRecurso(RecursoDto recurso);
    void eliminarRecurso(Recurso recurso);
    Recurso obtenerRecursoPorId(Integer recursoId);
    Recurso actualizarRecurso(Integer recursoId, Recurso recurso);
    List<Recurso> listarRecursosPorMateria(Integer materiaId);
    List<Recurso> buscarRecursosPorNombre(String nombre);
    List<Recurso> buscarRecursosPorTipo(String tipo);
    Recurso asignarRecursoAMateria(Integer recursoId, Materia materia);
    boolean validarRecursoUnico(RecursoDto recurso);
    List<Recurso> obtenerRecursosRecientes();
    List<Recurso> obtenerRecursosDestacados();

}

