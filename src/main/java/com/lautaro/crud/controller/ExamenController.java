package com.lautaro.crud.controller;

import com.lautaro.crud.dto.ExamenDto;
import com.lautaro.crud.service.ExamenService;
import com.lautaro.entity.examen.Ejercicio;
import com.lautaro.entity.examen.Examen;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/examenes")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenService examenService;

    @PostMapping
    public ResponseEntity<Examen> crearExamen(@RequestBody ExamenDto examen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crearExamen(examen));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Examen> obtenerExamenPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(examenService.obtenerExamenPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Examen>> obtenerTodosLosExamenes() {
        return ResponseEntity.ok(examenService.obtenerTodosLosExamenes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Examen> actualizarExamen(@PathVariable Integer id, @RequestBody Examen examen) {
        return ResponseEntity.ok(examenService.actualizarExamen(id, examen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarExamen(@PathVariable Integer id) {
        examenService.eliminarExamen(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{examenId}/ejercicios")
    public ResponseEntity<Void> agregarEjercicioAExamen(@PathVariable Integer examenId, @RequestBody Ejercicio ejercicio) {
        examenService.agregarEjercicioAExamen(examenId, ejercicio);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{examenId}/ejercicios/{ejercicioId}")
    public ResponseEntity<Void> removerEjercicioDeExamen(@PathVariable Integer examenId, @PathVariable Integer ejercicioId) {
        examenService.removerEjercicioDeExamen(examenId, ejercicioId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{examenId}/calificar")
    public ResponseEntity<Void> calificarExamen(@PathVariable Integer examenId) {
        examenService.calificarExamen(examenId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Examen>> buscarExamenesPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(examenService.buscarExamenesPorFecha(fecha));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Examen>> buscarExamenesPorEstudiante(@PathVariable Integer estudianteId) {
        return ResponseEntity.ok(examenService.buscarExamenesPorEstudiante(estudianteId));
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Examen>> buscarExamenesPorProfesor(@PathVariable Integer profesorId) {
        return ResponseEntity.ok(examenService.buscarExamenesPorProfesor(profesorId));
    }

    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<Examen>> buscarExamenesPorClase(@PathVariable Integer claseId) {
        return ResponseEntity.ok(examenService.buscarExamenesPorClase(claseId));
    }

    @GetMapping("/promedio/{estudianteId}")
    public ResponseEntity<Double> calcularPromedioExamenes(@PathVariable Integer estudianteId) {
        return ResponseEntity.ok(examenService.calcularPromedioExamenes(estudianteId));
    }
}