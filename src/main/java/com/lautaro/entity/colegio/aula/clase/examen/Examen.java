package com.lautaro.entity.colegio.aula.clase.examen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.clase.examen.enums.Dificultad;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "examenes", indexes = {
        @Index(name = "idx_examen_estudiante", columnList = "estudiante_id"),
        @Index(name = "idx_examen_clase", columnList = "clase_id"),
        @Index(name = "idx_examen_fecha", columnList = "fechaExamen")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La dificultad no puede ser nula")
    @Enumerated(EnumType.STRING)
    private Dificultad dificultad;

    @PositiveOrZero(message = "La nota debe ser un número positivo o cero")
    private Double nota;

    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ejercicio> ejercicios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    //Fecha del examen
    @NotNull
    private LocalDate fechaExamen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    @JsonIgnore
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clase_id")
    @JsonIgnore
    private Clase clase;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    public void calcularNota() {
        if (ejercicios.isEmpty()) {
            this.nota = 0.0;
            return;
        }

        double sumaPuntajes = ejercicios.stream()
                .mapToDouble(Ejercicio::getPuntajeObtenido)
                .sum();

        double puntajeTotal = ejercicios.stream()
                .mapToDouble(Ejercicio::getPuntajeMaximo)
                .sum();

        this.nota = (sumaPuntajes / puntajeTotal) * 10; // Asumiendo una escala de 0 a 10
    }

    public void agregarEjercicio(Ejercicio ejercicio) {
        ejercicios.add(ejercicio);
        ejercicio.setExamen(this);
        calcularNota();
    }

    public void removerEjercicio(Ejercicio ejercicio) {
        ejercicios.remove(ejercicio);
        ejercicio.setExamen(null);
        calcularNota();
    }

    // Método para verificar si todos los ejercicios están calificados
    public boolean estaTotalmenteCalificado() {
        return ejercicios.stream().allMatch(e -> e.getPuntajeObtenido() != null);
    }

}