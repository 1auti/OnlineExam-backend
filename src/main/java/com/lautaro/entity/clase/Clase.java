package com.lautaro.entity.clase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import com.lautaro.entity.aula.Aula;
import com.lautaro.entity.examen.Examen;
import com.lautaro.entity.materia.Materia;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class Clase extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    @JsonIgnore
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id")
    private Aula aula;

    @OneToOne(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Examen examen;

    @NotNull(message = "La fecha de la clase no puede ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaClase;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    public void setExamen(Examen examen) {
        if (examen == null) {
            if (this.examen != null) {
                this.examen.setClase(null);
            }
        } else {
            examen.setClase(this);
        }
        this.examen = examen;
    }

    public boolean tieneConflictoHorario(Clase otraClase) {
        return this.fechaClase.equals(otraClase.getFechaClase()) &&
                this.horaInicio.isBefore(otraClase.getHoraFin()) &&
                otraClase.getHoraInicio().isBefore(this.horaFin);
    }
}