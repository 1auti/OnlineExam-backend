package com.lautaro.crud.controller;

import com.lautaro.crud.dto.ProfesorDto;
import com.lautaro.crud.service.ProfesorService;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.persona.profesor.Profesor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorService.crearProfesor(profesor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> buscarProfesorPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(profesorService.buscarProfesorPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> buscarTodosProfesores() {
        return ResponseEntity.ok(profesorService.buscarTodosProfesores());
    }

    @PutMapping
    public ResponseEntity<Profesor> actualizarProfesor(@RequestBody Profesor profesor) {
        return ResponseEntity.ok(profesorService.actualizarProfesor(profesor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Integer id) {
        profesorService.eliminarProfesor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{profesorId}/clases/{claseId}")
    public ResponseEntity<Void> asignarClase(@PathVariable Integer profesorId, @PathVariable Integer claseId) {
        profesorService.asignarClase(profesorId, claseId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{profesorId}/clases/{claseId}")
    public ResponseEntity<Void> removerClase(@PathVariable Integer profesorId, @PathVariable Integer claseId) {
        profesorService.removerClase(profesorId, claseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{profesorId}/clases")
    public ResponseEntity<List<Clase>> buscarClasesDeProfesor(@PathVariable Integer profesorId) {
        return ResponseEntity.ok(profesorService.buscarClasesDeProfesor(profesorId));
    }

    @GetMapping("/{profesorId}/carga-horaria")
    public ResponseEntity<Integer> calcularCargaHoraria(@PathVariable Integer profesorId) {
        return ResponseEntity.ok(profesorService.calcularCargaHoraria(profesorId));
    }

    @GetMapping("/{profesorId}/carga-horaria-por-materia")
    public ResponseEntity<Map<String, Integer>> calcularCargaHorariaPorMateria(@PathVariable Integer profesorId) {
        return ResponseEntity.ok(profesorService.calcularCargaHorariaPorMateria(profesorId));
    }

    @GetMapping("/{profesorId}/verificar-disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @PathVariable Integer profesorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalTime fin) {
        return ResponseEntity.ok(profesorService.verificarDisponibilidad(profesorId, inicio, fin));
    }

    @GetMapping("/por-materia")
    public ResponseEntity<List<Profesor>> buscarProfesoresPorMateria(@RequestParam String materia) {
        return ResponseEntity.ok(profesorService.buscarProfesoresPorMateria(materia));
    }
}