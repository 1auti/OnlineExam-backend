package com.lautaro.crud.controller;
import com.lautaro.crud.dto.RecursoDto;
import com.lautaro.crud.service.RecursosService;
import com.lautaro.entity.recursos.Recurso;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
public class RecursoController {

    private final RecursosService recursosService;


    @PostMapping
    public ResponseEntity<Recurso> crearRecurso(@Validated @ModelAttribute RecursoDto recursoDto) {
        Recurso recursoCreado = recursosService.crearRecurso(recursoDto);
        return ResponseEntity.ok(recursoCreado);
    }


    @GetMapping("/{materiaId}")
    public ResponseEntity<List<Recurso>> listarRecursosPorMateria(@PathVariable Integer materiaId) {
        List<Recurso> recursos = recursosService.listarRecursosPorMateria(materiaId);
        return ResponseEntity.ok(recursos);
    }
}
