package com.lautaro.entity.examen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen,Integer> {
    List<Examen> findByFechaExamen(LocalDate fecha);
    @Query("SELECT e FROM Examen e JOIN e.estudiantes est WHERE est.id = :estudianteId")
    List<Examen> findByEstudiantes_Id(@Param("estudianteId") Integer estudianteId);
    List<Examen> findByProfesorId(Integer profesorId);
    List<Examen> findByClaseId(Integer claseId);
}
