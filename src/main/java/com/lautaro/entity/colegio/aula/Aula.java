package com.lautaro.entity.colegio.aula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.Colegio;
import com.lautaro.entity.colegio.aula.clase.Clase;
import com.lautaro.entity.colegio.aula.enums.Grado;
import com.lautaro.entity.colegio.aula.enums.Modalidad;
import com.lautaro.entity.persona.estudiante.Estudiante;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.logging.log4j.util.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "aulas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El grado no puede ser nulo")
    @Enumerated(EnumType.STRING)
    private Grado grado;

    @NotNull(message = "La modalidad no puede ser nula")
    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Positive
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    private int capacidadMaxima;
    @NotNull(message = "El año no puede ser nulo")
    @Min(value = 1, message = "El año debe ser al menos 1")
    @Max(value = 6, message = "El año no puede ser mayor a 6")
    private Integer anio;

    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Clase> clases = new ArrayList<>();

    @PositiveOrZero(message = "El promedio de clases debe ser un número positivo o cero")
    private Double promedioClases;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colegio_id")
    @JsonIgnore
    private Colegio colegio;

    @PositiveOrZero(message = "El rank debe ser un número positivo o cero")
    private Integer rank;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estudiante> estudiantes = new ArrayList<>();

    // Métodos helper para mantener la consistencia bidireccional
    public void addClase(Clase clase) {
        clases.add(clase);
        clase.setAula(this);
    }

    public void removeClase(Clase clase) {
        clases.remove(clase);
        clase.setAula(null);
    }

    public void calcularPromedioAula() {
        if (estudiantes.isEmpty()) {
            this.promedioClases = 0.0;
            return;
        }

        double sumaPromedios = estudiantes.stream()
                .mapToDouble(Estudiante::getPromedio)
                .sum();

        this.promedioClases = sumaPromedios / estudiantes.size();
    }


    public void calcularRankingEstudiantes() {
        // Ordenar los estudiantes del aula por su promedio en orden descendente
        List<Estudiante> todosEstudiantes = estudiantes.stream()
                .distinct()
                .sorted(Comparator.comparing(Estudiante::getPromedio).reversed())
                .collect(Collectors.toList());

        // Inicializar el ranking
        int rank = 1;
        double lastPromedio = -1;

        for (Estudiante estudiante : todosEstudiantes) {
            // Comparar el promedio actual con el último promedio
            if (Double.compare(estudiante.getPromedio(), lastPromedio) != 0) {
                rank = todosEstudiantes.indexOf(estudiante) + 1;
            }
            estudiante.setRankEnAula(rank);
            lastPromedio = estudiante.getPromedio();
        }
    }


    public boolean estaLlena() {
        // Contar el número de estudiantes en el aula directamente
        return estudiantes.size() >= capacidadMaxima;
    }



}