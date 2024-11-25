package com.lautaro.entity.materia;

import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.recursos.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia,Integer> {
  Materia findBynombreMateria(String nombreMateria);
  Materia findBynombreMateriaAndClase(String nombreMateria, Clase clase);
  List<Recurso> findByRecursos_Id(Integer recursoId);
  List<Materia> findByExamenes_Id(Integer examenId);
  @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
          "FROM Materia m " +
          "JOIN m.estudiantes e " +
          "WHERE e.id = :estudianteId AND m.id = :materiaId AND m.aprobada = TRUE")
  boolean isMateriaAprobadaByEstudiante(@Param("estudianteId") Integer estudianteId,
                                        @Param("materiaId") Integer materiaId);
  boolean existsByNombreMateriaAndColegioId(String nombreMateria, Integer colegioId);

}
