package com.lautaro.crud.service.impl;

import com.lautaro.crud.dto.EjercicioDto;
import com.lautaro.crud.service.EjercicioExamen;
import com.lautaro.entity.colegio.aula.clase.examen.Ejercicio;
import com.lautaro.entity.colegio.aula.clase.examen.EjercicioRepository;
import com.lautaro.entity.colegio.aula.clase.examen.Opcion;
import com.lautaro.entity.colegio.aula.clase.examen.OpcionRepository;
import com.lautaro.entity.colegio.aula.clase.examen.enums.Dificultad;
import com.lautaro.entity.colegio.aula.clase.examen.enums.TipoEjercicio;
import com.lautaro.entity.mapper.EjercicioMapper;
import com.lautaro.exception.ejercicio.EjercicioNotFoundException;
import com.lautaro.exception.ejercicio.InvalidEjercicioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EjercicioServiceImpl implements EjercicioExamen {

    private final EjercicioRepository ejercicioRepository;
    private final OpcionRepository opcionRepository;

    public Ejercicio crearEjercicio(EjercicioDto ejercicio) throws InvalidEjercicioException {
        Ejercicio ejercicio1 = EjercicioMapper.toEntity(ejercicio);
        return  ejercicioRepository.save(ejercicio1);
    }

    @Override
    public Ejercicio obtenerEjercicioPorId(Integer id) throws EjercicioNotFoundException {
        return ejercicioRepository.findById(id)
                .orElseThrow(() -> new EjercicioNotFoundException("No se encontró el ejercicio con ID: " + id));
    }

    @Override
    public List<Ejercicio> obtenerTodosLosEjercicios() {
        return ejercicioRepository.findAll();
    }

    @Override
    public Ejercicio actualizarEjercicio(Integer id, Ejercicio ejercicio) throws EjercicioNotFoundException, InvalidEjercicioException {
        Ejercicio ejercicioExistente = obtenerEjercicioPorId(id);
        validarEjercicio(ejercicio);

        ejercicioExistente.setEnunciado(ejercicio.getEnunciado());
        ejercicioExistente.setTipoEjercicio(ejercicio.getTipoEjercicio());
        ejercicioExistente.setPuntajeMaximo(ejercicio.getPuntajeMaximo());
        ejercicioExistente.setPuntajeObtenido(ejercicio.getPuntajeObtenido());
        ejercicioExistente.setDificultad(ejercicio.getDificultad());

        return ejercicioRepository.save(ejercicioExistente);
    }

    @Override
    @Transactional
    public void eliminarEjercicio(Integer id) throws EjercicioNotFoundException {
        if (!ejercicioRepository.existsById(id)) {
            throw new EjercicioNotFoundException("No se encontró el ejercicio con ID: " + id);
        }
        ejercicioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ejercicio agregarOpcionAEjercicio(Integer ejercicioId, Opcion opcion) throws EjercicioNotFoundException, InvalidEjercicioException {
        Ejercicio ejercicio = obtenerEjercicioPorId(ejercicioId);
        ejercicio.agregarOpcion(opcion);
        validarEjercicio(ejercicio);

        return ejercicioRepository.save(ejercicio);
    }

    @Override
    @Transactional
    public Ejercicio removerOpcionDeEjercicio(Integer ejercicioId, Integer opcionId) throws EjercicioNotFoundException, InvalidEjercicioException {
        Ejercicio ejercicio = obtenerEjercicioPorId(ejercicioId);
        ejercicio.getOpciones().removeIf(opcion -> opcion.getId().equals(opcionId));
        validarEjercicio(ejercicio);
        opcionRepository.deleteById(opcionId);
        return ejercicioRepository.save(ejercicio);
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosPorExamen(Integer examenId) {
        return ejercicioRepository.findByExamenId(examenId);
    }

    @Override
    public List<Ejercicio> buscarEjerciciosPorDificultad(Dificultad dificultad) {
        return ejercicioRepository.findByDificultad(dificultad);
    }

    @Override
    public List<Ejercicio> buscarEjerciciosPorTipo(TipoEjercicio tipoEjercicio) {
        return ejercicioRepository.findByTipoEjercicio(tipoEjercicio);
    }

    @Override
    public List<Ejercicio> buscarEjerciciosPorEnunciado(String palabraClave) {
        return ejercicioRepository.findByEnunciadoContainingIgnoreCase(palabraClave);
    }

    @Override
    public Double calcularPuntajePromedioPorExamen(Integer examenId) {
        List<Ejercicio> ejercicios = obtenerEjerciciosPorExamen(examenId);
        if (ejercicios.isEmpty()) {
            return 0.0;
        }
        double sumaPuntajes = ejercicios.stream()
                .mapToDouble(Ejercicio::getPuntajeObtenido)
                .sum();
        return sumaPuntajes / ejercicios.size();
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosSinResponder(Integer examenId) {
        return obtenerEjerciciosPorExamen(examenId).stream()
                .filter(ejercicio -> ejercicio.getPuntajeObtenido() == null || ejercicio.getPuntajeObtenido() == 0)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarPuntajeObtenido(Integer ejercicioId, Double nuevoPuntaje) throws EjercicioNotFoundException {
        Ejercicio ejercicio = obtenerEjercicioPorId(ejercicioId);
        ejercicio.setPuntajeObtenido(nuevoPuntaje);
        ejercicioRepository.save(ejercicio);
    }

    @Override
    public List<Ejercicio> obtenerEjerciciosOrdenadosPorDificultad(Integer examenId) {
        return obtenerEjerciciosPorExamen(examenId).stream()
                .sorted((e1, e2) -> e1.getDificultad().compareTo(e2.getDificultad()))
                .collect(Collectors.toList());
    }

    private void validarEjercicio(Ejercicio ejercicio) throws InvalidEjercicioException {
        if (ejercicio.getEnunciado() == null || ejercicio.getEnunciado().trim().isEmpty()) {
            throw new InvalidEjercicioException("El enunciado del ejercicio no puede estar vacío");
        }
        if (ejercicio.getPuntajeMaximo() == null || ejercicio.getPuntajeMaximo() < 0) {
            throw new InvalidEjercicioException("El puntaje máximo debe ser un número positivo");
        }
        if (ejercicio.getTipoEjercicio() == null) {
            throw new InvalidEjercicioException("El tipo de ejercicio no puede ser nulo");
        }
        // Aquí puedes agregar más validaciones según tus requerimientos
    }
}
