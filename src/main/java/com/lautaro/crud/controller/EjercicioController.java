package com.lautaro.crud.controller;

import com.lautaro.crud.dto.EjercicioDto;
import com.lautaro.crud.service.EjercicioExamen;
import com.lautaro.entity.examen.Ejercicio;
import com.lautaro.entity.examen.Opcion;
import com.lautaro.entity.examen.enums.Dificultad;
import com.lautaro.entity.examen.enums.TipoEjercicio;
import com.lautaro.exception.ejercicio.EjercicioNotFoundException;
import com.lautaro.exception.ejercicio.InvalidEjercicioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
@RequiredArgsConstructor
public class EjercicioController {

    private final EjercicioExamen ejercicioService;

    @PostMapping
    public ResponseEntity<Ejercicio> crearEjercicio(@RequestBody EjercicioDto ejercicio) throws InvalidEjercicioException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ejercicioService.crearEjercicio(ejercicio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejercicio> obtenerEjercicioPorId(@PathVariable Integer id) throws EjercicioNotFoundException {
        return ResponseEntity.ok(ejercicioService.obtenerEjercicioPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Ejercicio>> obtenerTodosLosEjercicios() {
        return ResponseEntity.ok(ejercicioService.obtenerTodosLosEjercicios());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ejercicio> actualizarEjercicio(@PathVariable Integer id, @RequestBody Ejercicio ejercicio)
            throws EjercicioNotFoundException, InvalidEjercicioException {
        return ResponseEntity.ok(ejercicioService.actualizarEjercicio(id, ejercicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjercicio(@PathVariable Integer id) throws EjercicioNotFoundException {
        ejercicioService.eliminarEjercicio(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ejercicioId}/opciones")
    public ResponseEntity<Ejercicio> agregarOpcionAEjercicio(@PathVariable Integer ejercicioId, @RequestBody Opcion opcion)
            throws EjercicioNotFoundException, InvalidEjercicioException {
        return ResponseEntity.status(HttpStatus.CREATED).body(ejercicioService.agregarOpcionAEjercicio(ejercicioId, opcion));
    }

    @DeleteMapping("/{ejercicioId}/opciones/{opcionId}")
    public ResponseEntity<Ejercicio> removerOpcionDeEjercicio(@PathVariable Integer ejercicioId, @PathVariable Integer opcionId)
            throws EjercicioNotFoundException, InvalidEjercicioException {
        return ResponseEntity.ok(ejercicioService.removerOpcionDeEjercicio(ejercicioId, opcionId));
    }

    @GetMapping("/examen/{examenId}")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosPorExamen(@PathVariable Integer examenId) {
        return ResponseEntity.ok(ejercicioService.obtenerEjerciciosPorExamen(examenId));
    }

    @GetMapping("/dificultad/{dificultad}")
    public ResponseEntity<List<Ejercicio>> buscarEjerciciosPorDificultad(@PathVariable Dificultad dificultad) {
        return ResponseEntity.ok(ejercicioService.buscarEjerciciosPorDificultad(dificultad));
    }

    @GetMapping("/tipo/{tipoEjercicio}")
    public ResponseEntity<List<Ejercicio>> buscarEjerciciosPorTipo(@PathVariable TipoEjercicio tipoEjercicio) {
        return ResponseEntity.ok(ejercicioService.buscarEjerciciosPorTipo(tipoEjercicio));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Ejercicio>> buscarEjerciciosPorEnunciado(@RequestParam String palabraClave) {
        return ResponseEntity.ok(ejercicioService.buscarEjerciciosPorEnunciado(palabraClave));
    }

    @GetMapping("/examen/{examenId}/puntaje-promedio")
    public ResponseEntity<Double> calcularPuntajePromedioPorExamen(@PathVariable Integer examenId) {
        return ResponseEntity.ok(ejercicioService.calcularPuntajePromedioPorExamen(examenId));
    }

    @GetMapping("/examen/{examenId}/sin-responder")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosSinResponder(@PathVariable Integer examenId) {
        return ResponseEntity.ok(ejercicioService.obtenerEjerciciosSinResponder(examenId));
    }

    @PatchMapping("/{ejercicioId}/puntaje")
    public ResponseEntity<Void> actualizarPuntajeObtenido(@PathVariable Integer ejercicioId, @RequestParam Double nuevoPuntaje)
            throws EjercicioNotFoundException {
        ejercicioService.actualizarPuntajeObtenido(ejercicioId, nuevoPuntaje);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/examen/{examenId}/ordenados-por-dificultad")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosOrdenadosPorDificultad(@PathVariable Integer examenId) {
        return ResponseEntity.ok(ejercicioService.obtenerEjerciciosOrdenadosPorDificultad(examenId));
    }
}
