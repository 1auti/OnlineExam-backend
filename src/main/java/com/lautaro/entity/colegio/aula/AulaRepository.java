package com.lautaro.entity.colegio.aula;

import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula,Integer> {
    List<Aula> findByGradoAndModalidad(Grado grado, Modalidad modalidad);
    List<Aula> findByColegioId(Integer colegioId);
}
