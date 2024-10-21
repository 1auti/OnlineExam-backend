package com.lautaro.entity.colegio.aula.clase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase,Integer> {

    List<Clase> findByFechaClase(LocalDate fecha);

    List<Clase> findByProfesorId(Integer profesorId);

    List<Clase> findByAulaId(Integer aulaId);

    Clase findByMateriaAndFecha(String materia,LocalDate fecha);

    @Query("SELECT c FROM Clase c WHERE c.aula.id = :aulaId AND c.fechaClase = :fecha AND " +
            "((c.horaInicio <= :horaInicio AND c.horaFin > :horaInicio) OR " +
            "(c.horaInicio < :horaFin AND c.horaFin >= :horaFin) OR " +
            "(c.horaInicio >= :horaInicio AND c.horaFin <= :horaFin))")
    List<Clase> findClasesConConflictoHorarioPorAula(@Param("aulaId") Integer aulaId,
                                                     @Param("fecha") LocalDate fecha,
                                                     @Param("horaInicio") LocalDateTime horaInicio,
                                                     @Param("horaFin") LocalDateTime horaFin);
}


