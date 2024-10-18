package com.lautaro.crud.controller;

import com.lautaro.crud.dto.EstudianteDto;
import com.lautaro.crud.service.EstudianteService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody EstudianteDto estudiante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.crearEstudiante(estudiante));
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> buscarEstudiantes() {
        return ResponseEntity.ok(estudianteService.buscarEstudiantes());
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<Estudiante> buscarEstudiantePorCardId(@PathVariable Long cardId) {
        return ResponseEntity.ok(estudianteService.buscarEstudiantePorCardId(cardId));
    }

    @PostMapping("/{cardId}/examenes")
    public ResponseEntity<Void> agregarExamen(@PathVariable Long cardId, @RequestBody Examen examen) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        estudianteService.agregarExamen(estudiante, examen);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardId}/examenes/{examenId}")
    public ResponseEntity<Void> removerExamen(@PathVariable Long cardId, @PathVariable Integer examenId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        Examen examen = new Examen();
        examen.setId(examenId);
        estudianteService.removerExamen(estudiante, examen);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cardId}/promedio-por-materia")
    public ResponseEntity<Map<String, Double>> calcularPromedioPorMateria(@PathVariable Long cardId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        return ResponseEntity.ok(estudianteService.calcularPromedioPorMateria(estudiante));
    }

    @PostMapping("/{cardId}/matricular")
    public ResponseEntity<Void> matricularEstudiante(@PathVariable Long cardId, @RequestBody Aula aula) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        estudianteService.asignarEstudianteAula(estudiante,aula);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardId}/dar-de-baja")
    public ResponseEntity<Void> darDeBajaEstudiante(@PathVariable Long cardId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        estudianteService.removerEstudianteDeAula(estudiante);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/actualizar-ranking")
    public ResponseEntity<Void> actualizarRanking(@RequestBody Colegio colegio) {
        estudianteService.actualizarRanking(colegio);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cardId}/rendimiento-comparativo")
    public ResponseEntity<Map<String, Double>> obtenerRendimientoComparativo(@PathVariable Long cardId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId);
        return ResponseEntity.ok(estudianteService.obtenerRendimientoComparativo(estudiante));
    }

    @GetMapping("/por-rendimiento")
    public ResponseEntity<List<Estudiante>> buscarEstudiantesPorRendimiento(
            @RequestBody Colegio colegio,
            @RequestParam double promedioMinimo) {
        return ResponseEntity.ok(estudianteService.buscarEstudiantesPorRendimiento(colegio, promedioMinimo));
    }
}
