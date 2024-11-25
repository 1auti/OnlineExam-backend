package com.lautaro.entity.recursos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso,Integer> {
    List<Recurso> findByNombreContainingIgnoreCase(String nombre);
    List<Recurso> findByTipo(String tipo);
    boolean existsByNombreAndFormato(String nombre, Formato formato);
    List<Recurso> findTop5ByOrderByFechaCreacionDesc();
    List<Recurso> findTop5ByDestacadoTrue();
}
