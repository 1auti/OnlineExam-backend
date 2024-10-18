package com.lautaro.entity.colegio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ColegioRepository extends JpaRepository<Colegio,Integer> {

    Optional<Colegio> findByNombre(String nombre);
}
