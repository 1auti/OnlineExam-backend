package com.lautaro.crud.controller;

import com.lautaro.crud.service.AulasService;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@RequiredArgsConstructor
public class AulasController {

    private final AulasService aulasService;

    @PostMapping
    public ResponseEntity<Aula> crearAula(@RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulasService.crearAula(aula));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aula> obtenerAulaPorId(@PathVariable Integer id) {
        return aulasService.obtenerAulaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Aula>> obtenerTodasLasAulas() {
        return ResponseEntity.ok(aulasService.obtenerTodasLasAulas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aula> actualizarAula(@PathVariable Integer id, @RequestBody Aula aulaActualizada) {
        return ResponseEntity.ok(aulasService.actualizarAula(id, aulaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAula(@PathVariable Integer id) {
        aulasService.eliminarAula(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Aula>> buscarAulasPorGradoYModalidad(
            @RequestParam Grado grado,
            @RequestParam Modalidad modalidad) {
        return ResponseEntity.ok(aulasService.buscarAulasPorGradoYModalidad(grado, modalidad));
    }

    @PostMapping("/{id}/calcular-promedio-clases")
    public ResponseEntity<Void> calcularPromedioClases(@PathVariable Integer id) {
        aulasService.calcularPromedioClases(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/calcular-ranking-estudiantes")
    public ResponseEntity<Void> calcularRankingEstudiantes(@PathVariable Integer id) {
        aulasService.calcularRankingEstudiantes(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/verificar-capacidad")
    public ResponseEntity<Boolean> verificarCapacidad(@PathVariable Integer id) {
        return ResponseEntity.ok(aulasService.verificarCapacidad(id));
    }

    @GetMapping("/colegio/{colegioId}")
    public ResponseEntity<List<Aula>> obtenerAulasPorColegio(@PathVariable Integer colegioId) {
        return ResponseEntity.ok(aulasService.obtenerAulasPorColegio(colegioId));
    }
}
