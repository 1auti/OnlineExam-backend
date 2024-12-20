package com.lautaro.entity.examen;

import com.lautaro.entity.examen.enums.Dificultad;
import com.lautaro.entity.examen.enums.TipoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Integer> {
    List<Ejercicio> findByExamenId(Integer examenId);
    List<Ejercicio> findByTipoEjercicio(TipoEjercicio tipo);
    List<Ejercicio> findByDificultad(Dificultad dificultad);
    List<Ejercicio> findByEnunciadoContainingIgnoreCase(String palabraClave);
}
