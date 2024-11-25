package com.lautaro.entity.persona.estudiante;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.Persona;
import com.lautaro.entity.examen.Examen;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Lazy;

import java.util.ArrayList;
import java.util.List;

@Entity
//Los índices de base de datos mejoran el rendimiento de las consultas. Se usan en columnas que se consultan frecuentemente
@Table(name = "estudiantes")
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

    @ManyToMany(mappedBy = "materias",cascade = CascadeType.ALL,fetch = FetchType.LAZY )
    private List<Materia> materias;
    @ManyToMany
    @JoinTable(
            name = "estudiante_examen",
            joinColumns = @JoinColumn(name = "estudiante_id"),
            inverseJoinColumns = @JoinColumn(name = "examen_id")
    )
    private List<Examen> examenes = new ArrayList<>();


    public void agregarExamen(Examen examen){
        this.getExamenes().add(examen);
        examen.getEstudiantes().add(this);
    }


}