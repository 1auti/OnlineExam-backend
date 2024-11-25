package com.lautaro.entity.colegio;

import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ColegioRepository extends JpaRepository<Colegio,Integer> {

    Optional<Colegio> findByNombre(String nombre);

    // Obtener lista de estudiantes por colegio
    @Query("SELECT c.estudiantes FROM Colegio c WHERE c.id = :colegioId")
    List<Estudiante> findEstudiantesByColegioId(@Param("colegioId") Integer colegioId);

    // Obtener lista de materias por colegio
    @Query("SELECT c.materias FROM Colegio c WHERE c.id = :colegioId")
    List<Materia> findMateriasByColegioId(@Param("colegioId") Integer colegioId);

    // Obtener lista de profesores por colegio
    @Query("SELECT c.profesores FROM Colegio c WHERE c.id = :colegioId")
    Set<Profesor> findProfesoresByColegioId(@Param("colegioId") Integer colegioId);

    // Obtener lista de aulas por colegio
    @Query("SELECT c.aulas FROM Colegio c WHERE c.id = :colegioId")
    List<Aula> findAulasByColegioId(@Param("colegioId") Integer colegioId);
}
