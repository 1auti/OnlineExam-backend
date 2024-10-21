package com.lautaro.crud.service;

import com.lautaro.crud.dto.EjercicioDto;
import com.lautaro.entity.colegio.aula.clase.examen.Ejercicio;
import com.lautaro.entity.colegio.aula.clase.examen.Opcion;
import com.lautaro.entity.colegio.aula.clase.examen.enums.Dificultad;
import com.lautaro.entity.colegio.aula.clase.examen.enums.TipoEjercicio;
import com.lautaro.exception.ejercicio.EjercicioNotFoundException;
import com.lautaro.exception.ejercicio.InvalidEjercicioException;

import java.util.List;

public interface EjercicioExamen {
    Ejercicio crearEjercicio(EjercicioDto ejercicio) throws InvalidEjercicioException;
    Ejercicio obtenerEjercicioPorId(Integer id) throws EjercicioNotFoundException;
    List<Ejercicio> obtenerTodosLosEjercicios();
    Ejercicio actualizarEjercicio(Integer id, Ejercicio ejercicio) throws EjercicioNotFoundException, InvalidEjercicioException;
    void eliminarEjercicio(Integer id) throws EjercicioNotFoundException;
    Ejercicio agregarOpcionAEjercicio(Integer ejercicioId, Opcion opcion) throws EjercicioNotFoundException, InvalidEjercicioException;
    Ejercicio removerOpcionDeEjercicio(Integer ejercicioId, Integer opcionId) throws EjercicioNotFoundException, InvalidEjercicioException;
    List<Ejercicio> obtenerEjerciciosPorExamen(Integer examenId);
    List<Ejercicio> buscarEjerciciosPorDificultad(Dificultad dificultad);
    List<Ejercicio> buscarEjerciciosPorTipo(TipoEjercicio tipoEjercicio);
    List<Ejercicio> buscarEjerciciosPorEnunciado(String palabraClave);
    Double calcularPuntajePromedioPorExamen(Integer examenId);
    List<Ejercicio> obtenerEjerciciosSinResponder(Integer examenId);
    void actualizarPuntajeObtenido(Integer ejercicioId, Double nuevoPuntaje) throws EjercicioNotFoundException;
    List<Ejercicio> obtenerEjerciciosOrdenadosPorDificultad(Integer examenId);
}
