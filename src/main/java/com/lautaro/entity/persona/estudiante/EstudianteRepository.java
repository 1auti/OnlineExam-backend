package com.lautaro.entity.persona.estudiante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante,Integer> {

    Optional<Estudiante> findByCardId(Long cardId);
}
