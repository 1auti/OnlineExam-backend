package com.lautaro.entity.examen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import com.lautaro.entity.clase.Clase;
import com.lautaro.entity.examen.enums.Dificultad;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "examenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Examen extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La dificultad no puede ser nula")
    @Enumerated(EnumType.STRING)
    private Dificultad dificultad;

    @PositiveOrZero(message = "La nota debe ser un número positivo o cero")
    @Max(10)
    @Min(0)
    private Double nota;

    private Boolean aprobado;

    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ejercicio> ejercicios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    //Fecha del examen
    @NotNull
    private LocalDateTime fechaExamen;

    @ManyToMany(mappedBy = "examenes")
    @JsonIgnore
    private List<Estudiante> estudiantes = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clase_id")
    @JsonIgnore
    private Clase clase;

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

    public void EstaAprobado(){
        if (nota >= 7){
            aprobado = true;
        }else{
            aprobado = false;
        }
    }

    public void setClase(Clase clase) {
        if (this.clase != null) {
            this.clase.setExamen(null);
        }
        this.clase = clase;
        if (clase != null) {
            clase.setExamen(this);
        }
    }

}