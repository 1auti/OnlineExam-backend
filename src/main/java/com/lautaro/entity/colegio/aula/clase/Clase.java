package com.lautaro.entity.colegio.aula.clase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.colegio.aula.clase.examen.Examen;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clases", indexes = {
        @Index(name = "idx_clase_profesor", columnList = "profesor_id"),
        @Index(name = "idx_clase_aula", columnList = "aula_id"),
        @Index(name = "idx_clase_fecha", columnList = "fechaClase")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La materia no puede estar vacía")
    private String materia;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnore
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private Aula aula;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Examen> examenes = new ArrayList<>(); // Relación uno a muchos con Examen

    @NotNull(message = "La fecha de la clase no puede ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaClase;

    //Horarias de las clases
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


    public void addExamen(Examen examen) {
        examenes.add(examen);
        examen.setClase(this); // Se actualiza la relación en el lado de Examen
    }

    public void removeExamen(Examen examen) {
        examenes.remove(examen);
        examen.setClase(null); // Se elimina la referencia a la clase en Examen
    }



    // Método para verificar si hay conflicto de horarios
    public boolean tieneConflictoHorario(Clase otraClase) {
        return this.fechaClase.equals(otraClase.getFechaClase()) &&
                this.horaInicio.isBefore(otraClase.getHoraFin()) &&
                otraClase.getHoraInicio().isBefore(this.horaFin);
    }


}
