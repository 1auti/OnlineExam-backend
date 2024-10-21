package com.lautaro.entity.colegio.aula;

import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import com.lautaro.entity.persona.estudiante.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AulaRepository extends JpaRepository<Aula,Integer> {
    List<Aula> findByGradoAndModalidad(Grado grado, Modalidad modalidad);
    List<Aula> findByColegioId(Integer colegioId);
    Optional<Aula> findByColegioIdAndAnioAndGradoAndModalidad(Integer colegioId, Integer anio, Grado grado, Modalidad modalidad);
}
