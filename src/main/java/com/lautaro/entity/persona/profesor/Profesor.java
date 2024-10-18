package com.lautaro.entity.persona.profesor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.persona.Persona;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "profesores", indexes = {
        @Index(name = "idx_profesor_colegio", columnList = "colegio_id"),
        @Index(name = "idx_profesor_departamento", columnList = "departamento")
})
@Data
@NoArgsConstructor
public class Profesor extends Persona {

    private String titulo;

    private String departamento;

    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Clase> clases; // Relación uno a muchos


    @ManyToOne
    @JoinColumn(name = "colegio_id")
    @JsonIgnore
    private Colegio colegio; // Relación muchos a uno con Colegio




}
