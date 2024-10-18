package com.lautaro.crud.controller;

import com.lautaro.crud.service.ColegioService;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colegios")
@RequiredArgsConstructor
public class ColegioController {

    private final ColegioService colegioService;

    @PostMapping
    public ResponseEntity<Colegio> crearColegio(@RequestBody Colegio colegio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colegioService.crearColegio(colegio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colegio> buscarColegioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colegioService.buscarColegioPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Colegio>> buscarTodosColegios() {
        return ResponseEntity.ok(colegioService.buscarTodosColegios());
    }

    @PutMapping
    public ResponseEntity<Colegio> actualizarColegio(@RequestBody Colegio colegio) {
        return ResponseEntity.ok(colegioService.actualizarColegio(colegio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColegio(@PathVariable Integer id) {
        colegioService.eliminarColegio(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{colegioId}/profesores")
    public ResponseEntity<Void> agregarProfesor(@PathVariable Integer colegioId, @RequestBody Profesor profesor) {
        colegioService.agregarProfesor(colegioId, profesor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{colegioId}/profesores/{profesorId}")
    public ResponseEntity<Void> removerProfesor(@PathVariable Integer colegioId, @PathVariable Integer profesorId) {
        colegioService.removerProfesor(colegioId, profesorId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{colegioId}/estudiantes")
    public ResponseEntity<Void> agregarEstudiante(@PathVariable Integer colegioId, @RequestBody Estudiante estudiante) {
        colegioService.agregarEstudiante(colegioId, estudiante);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{colegioId}/estudiantes/{estudianteCardId}")
    public ResponseEntity<Void> removerEstudiante(@PathVariable Integer colegioId, @PathVariable Long estudianteCardId) {
        colegioService.removerEstudiante(colegioId, estudianteCardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{colegioId}/aulas")
    public ResponseEntity<Void> agregarAula(@PathVariable Integer colegioId, @RequestBody Aula aula) {
        colegioService.agregarAula(colegioId, aula);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{colegioId}/aulas/{aulaId}")
    public ResponseEntity<Void> removerAula(@PathVariable Integer colegioId, @PathVariable Integer aulaId) {
        colegioService.removerAula(colegioId, aulaId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{colegioId}/actualizar-estadisticas")
    public ResponseEntity<Void> actualizarEstadisticas(@PathVariable Integer colegioId) {
        colegioService.actualizarEstadisticas(colegioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{colegioId}/estudiantes/{estudianteCardId}/promedio")
    public ResponseEntity<Void> actualizarPromedioEstudiante(
            @PathVariable Integer colegioId,
            @PathVariable Long estudianteCardId,
            @RequestParam double promedioAnterior,
            @RequestParam double promedioNuevo) {
        colegioService.actualizarPromedioEstudiante(colegioId, estudianteCardId, promedioAnterior, promedioNuevo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{colegioId}/ranking-estudiantes")
    public ResponseEntity<List<Estudiante>> obtenerRankingEstudiantes(@PathVariable Integer colegioId) {
        return ResponseEntity.ok(colegioService.obtenerRankingEstudiantes(colegioId));
    }

    @GetMapping("/{colegioId}/ranking-aulas")
    public ResponseEntity<List<Aula>> obtenerRankingAulas(@PathVariable Integer colegioId) {
        return ResponseEntity.ok(colegioService.obtenerRankingAulas(colegioId));
    }
}