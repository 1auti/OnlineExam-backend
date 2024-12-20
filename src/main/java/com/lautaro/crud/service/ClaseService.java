package com.lautaro.crud.service;

import com.lautaro.crud.dto.ClaseDto;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.examen.Examen;
import com.lautaro.entity.materia.Materia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClaseService {

    Clase crearClase(ClaseDto clase);
    Optional<Clase> obtenerClasePorId(Integer id);
    List<Clase> obtenerTodasLasClases();
    Clase actualizarClase(Integer id, Clase claseActualizada);
    void eliminarClase(Integer id);
    void asignarProfesorAClase(Integer claseId, Integer profesorId);
    void eliminarProfesorAClase(Integer claseId,Integer profesorId);
    void agregarExamenAClase(Integer claseId, Examen examen);
    void removerExamenDeClase(Integer claseId, Integer examenId);
    List<Clase> buscarClasesPorFecha(LocalDate fecha);
    List<Clase> buscarClasesPorProfesor(Integer profesorId);
    List<Clase> buscarClasesPorAula(Integer aulaId);
    boolean verificarConflictoHorario(Clase nuevaClase);
    List<Clase> traerClasesPorMateria(Materia materia);

}
