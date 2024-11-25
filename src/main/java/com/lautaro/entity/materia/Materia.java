package com.lautaro.entity.materia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import com.lautaro.entity.recursos.Recurso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Materia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La materia no puede estar vacía")
    private String nombreMateria;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "materias",cascade = CascadeType.ALL)
    private List<Clase> clases = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "profesor_id")
    @JsonIgnore
    private Profesor profesor;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Recurso> recursos  = new ArrayList<>();

    @ManyToMany(mappedBy = "materia")
    @JsonIgnore
    private List<Estudiante> estudiantes  = new ArrayList<>();

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Examen> examenes  = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "colegio_id")
    @JsonIgnore
    private Colegio colegio;

    private Boolean aprobada = false;


    public boolean estaAprobado(){
        return aprobada;
    }

    public void agregarRecurso(Recurso recurso){
        recursos.add(recurso);
        recurso.setMateria(this);
    }

    public void eliminarRecurso(Recurso recurso){
        recursos.remove(recurso);
        recurso.setMateria(null);
    }
}