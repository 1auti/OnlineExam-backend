package com.lautaro.crud.controller;

import com.lautaro.crud.dto.EstudianteDto;
import com.lautaro.crud.service.EstudianteService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.examen.Examen;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Integer id) {
        boolean eliminado = estudianteService.eliminarEstudiante(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{colegioId}/{cardId}")
    public ResponseEntity<Estudiante> actualizarEstudiante(@PathVariable Integer colegioId, @PathVariable Long cardId, @RequestBody EstudianteDto estudianteDto) {
        return ResponseEntity.ok(estudianteService.actualizarEstudiante(cardId, estudianteDto));
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> buscarEstudiantes() {
        return ResponseEntity.ok(estudianteService.buscarEstudiantes());
    }

    @GetMapping("/{colegioId}/{cardId}")
    public ResponseEntity<Estudiante> buscarEstudiantePorCardId(@PathVariable Long cardId, @PathVariable Integer colegioId) {
        return ResponseEntity.ok(estudianteService.buscarEstudiantePorCardId(cardId, colegioId));
    }

    @PostMapping("/{colegioId}/{cardId}/examenes")
    public ResponseEntity<Void> agregarExamen(@PathVariable Long cardId, @PathVariable Integer colegioId, @RequestBody Examen examen) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        estudianteService.agregarExamen(estudiante, examen);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{colegioId}/{cardId}/examenes/{examenId}")
    public ResponseEntity<Void> removerExamen(@PathVariable Long cardId, @PathVariable Integer colegioId, @PathVariable Integer examenId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        Examen examen = new Examen();
        examen.setId(examenId);
        estudianteService.removerExamen(estudiante, examen);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{colegioId}/{cardId}/promedio-por-materia")
    public ResponseEntity<Map<String, Double>> calcularPromedioPorMateria(@PathVariable Long cardId, @PathVariable Integer colegioId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        return ResponseEntity.ok(estudianteService.calcularPromedioPorMateria(estudiante));
    }

    @PostMapping("/{colegioId}/{cardId}/matricular")
    public ResponseEntity<Void> matricularEstudiante(@PathVariable Long cardId, @PathVariable Integer colegioId, @RequestBody Aula aula) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        estudianteService.asignarEstudianteAula(estudiante, aula);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{colegioId}/{cardId}/dar-de-baja")
    public ResponseEntity<Void> darDeBajaEstudiante(@PathVariable Long cardId, @PathVariable Integer colegioId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        estudianteService.removerEstudianteDeAula(estudiante);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/actualizar-ranking")
    public ResponseEntity<Void> actualizarRanking(@RequestBody Colegio colegio) {
        estudianteService.actualizarRanking(colegio);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{colegioId}/{cardId}/rendimiento-comparativo")
    public ResponseEntity<Map<String, Double>> obtenerRendimientoComparativo(@PathVariable Long cardId, @PathVariable Integer colegioId) {
        Estudiante estudiante = estudianteService.buscarEstudiantePorCardId(cardId, colegioId);
        return ResponseEntity.ok(estudianteService.obtenerRendimientoComparativo(estudiante));
    }

    @GetMapping("/por-rendimiento")
    public ResponseEntity<List<Estudiante>> buscarEstudiantesPorRendimiento(
            @RequestBody Colegio colegio,
            @RequestParam double promedioMinimo) {
        return ResponseEntity.ok(estudianteService.buscarEstudiantesPorRendimiento(colegio, promedioMinimo));
    }
}