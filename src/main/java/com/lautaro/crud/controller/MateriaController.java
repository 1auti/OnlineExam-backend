package com.lautaro.crud.controller;

import com.lautaro.crud.dto.MateriaDto;
import com.lautaro.crud.service.MateriaService;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.recursos.Recurso;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {


    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<Materia> crearMateria(@RequestBody MateriaDto materiaDto) {
        Materia nuevaMateria = materiaService.crearMateria(materiaDto);
        return ResponseEntity.ok(nuevaMateria);
    }

    @DeleteMapping("/{materiaId}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Integer materiaId) {
        materiaService.eliminarMateria(materiaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Materia> buscarMateria(
            @RequestParam String nombreMateria,
            @RequestParam Integer claseId
    ) {
        Clase clase = new Clase(); // Puedes usar un servicio para obtener la clase por ID si es necesario
        clase.setId(claseId);
        Materia materia = materiaService.buscarMateria(nombreMateria, clase);
        return ResponseEntity.ok(materia);
    }

    @PutMapping
    public ResponseEntity<Materia> editarMateria(@RequestBody Materia materia) {
        Materia materiaEditada = materiaService.editarMateria(materia);
        return ResponseEntity.ok(materiaEditada);
    }

    @PostMapping("/{materiaId}/asignar-estudiante")
    public ResponseEntity<Estudiante> asignarEstudiante(
            @PathVariable Integer materiaId,
            @RequestBody Estudiante estudiante
    ) {
        Materia materia = new Materia(); // Puedes usar un servicio para obtener la materia por ID si es necesario
        materia.setId(materiaId);
        Estudiante estudianteAsignado = materiaService.asignarEstudiante(estudiante, materia);
        return ResponseEntity.ok(estudianteAsignado);
    }

    @DeleteMapping("/{materiaId}/remover-estudiante/{estudianteId}")
    public ResponseEntity<Void> removerEstudiante(
            @PathVariable Integer materiaId,
            @PathVariable Integer estudianteId
    ) {
        Materia materia = new Materia(); // Puedes usar un servicio para obtener la materia por ID si es necesario
        materia.setId(materiaId);
        materiaService.removerEstudiante(estudianteId, materia);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{materiaId}/recursos")
    public ResponseEntity<List<Recurso>> traerRecursos(@PathVariable Integer materiaId) {
        List<Recurso> recursos = materiaService.traerRecursos(materiaId);
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/{materiaId}/examenes")
    public ResponseEntity<List<Materia>> traerExamenes(@PathVariable Integer materiaId) {
        List<Materia> examenes = materiaService.traerExamenes(materiaId);
        return ResponseEntity.ok(examenes);
    }
}
