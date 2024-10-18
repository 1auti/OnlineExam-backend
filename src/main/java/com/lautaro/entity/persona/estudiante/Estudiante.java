package com.lautaro.entity.persona.estudiante;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.persona.Persona;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
//Los índices de base de datos mejoran el rendimiento de las consultas. Se usan en columnas que se consultan frecuentemente
@Table(name = "estudiantes", indexes = {
        @Index(name = "idx_estudiante_colegio", columnList = "colegio_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Estudiante extends Persona {


    private Long cardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    @JsonIgnore
    private Aula aula;

    @PositiveOrZero(message = "El promedio debe ser un número positivo o cero")
    private Double promedio;

    @PositiveOrZero(message = "El rank debe ser un número positivo o cero")
    private Integer rankEnAula;
    @PositiveOrZero(message = "El rank debe ser un número positivo o cero")
    private Integer rankEnColegio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    @JsonIgnore
    private Colegio colegio;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Examen> examenes = new ArrayList<>();

}