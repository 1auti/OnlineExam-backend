package com.lautaro.crud.controller;

import com.lautaro.crud.dto.ClaseDto;
import com.lautaro.crud.service.ClaseService;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.examen.Examen;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
public class ClaseController {

    private final ClaseService claseService;

    @PostMapping
    public ResponseEntity<Clase> crearClase(@RequestBody ClaseDto clase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claseService.crearClase(clase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clase> obtenerClasePorId(@PathVariable Integer id) {
        return claseService.obtenerClasePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Clase>> obtenerTodasLasClases() {
        return ResponseEntity.ok(claseService.obtenerTodasLasClases());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clase> actualizarClase(@PathVariable Integer id, @RequestBody Clase clase) {
        return ResponseEntity.ok(claseService.actualizarClase(id, clase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Integer id) {
        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{claseId}/profesores/{profesorId}")
    public ResponseEntity<Void> asignarProfesorAClase(@PathVariable Integer claseId, @PathVariable Integer profesorId) {
        claseService.asignarProfesorAClase(claseId, profesorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{claseId}/examenes")
    public ResponseEntity<Void> agregarExamenAClase(@PathVariable Integer claseId, @RequestBody Examen examen) {
        claseService.agregarExamenAClase(claseId, examen);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{claseId}/examenes/{examenId}")
    public ResponseEntity<Void> removerExamenDeClase(@PathVariable Integer claseId, @PathVariable Integer examenId) {
        claseService.removerExamenDeClase(claseId, examenId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Clase>> buscarClasesPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(claseService.buscarClasesPorFecha(fecha));
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Clase>> buscarClasesPorProfesor(@PathVariable Integer profesorId) {
        return ResponseEntity.ok(claseService.buscarClasesPorProfesor(profesorId));
    }

    @GetMapping("/aula/{aulaId}")
    public ResponseEntity<List<Clase>> buscarClasesPorAula(@PathVariable Integer aulaId) {
        return ResponseEntity.ok(claseService.buscarClasesPorAula(aulaId));
    }

    @PostMapping("/verificar-conflicto")
    public ResponseEntity<Boolean> verificarConflictoHorario(@RequestBody Clase nuevaClase) {
        return ResponseEntity.ok(claseService.verificarConflictoHorario(nuevaClase));
    }
}
