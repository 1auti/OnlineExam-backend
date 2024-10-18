package com.lautaro.entity.colegio.aula.clase.examen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Integer> {
    List<Examen> findByFechaExamen(LocalDate fecha);
    List<Examen> findByEstudianteId(Integer estudianteId);
    List<Examen> findByProfesorId(Integer profesorId);
    List<Examen> findByClaseId(Integer claseId);
}
