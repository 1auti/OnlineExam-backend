package com.lautaro.entity.colegio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.entity.colegio.aula.Aula;
import com.lautaro.entity.persona.estudiante.Estudiante;
import com.lautaro.entity.persona.profesor.Profesor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "colegios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Colegio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del colegio no puede estar vacío")
    @Size(max = 100, message = "El nombre del colegio no puede exceder los 100 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotBlank(message = "La direccion no puede estar vacia")
    @Column(name = "direccion",unique = true )
    private String direccion;

    @PositiveOrZero(message = "El promedio del colegio debe ser un número positivo o cero")
    private Double promedioColegio;

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Profesor> profesores = new HashSet<>();

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estudiante> estudiantes = new ArrayList<>();

    @OneToMany(mappedBy = "colegio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aula> aulas = new ArrayList<>();

    private double sumaPromedios = 0.0;
    private int contadorEstudiantes = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


    // Métodos helper para mantener la consistencia bidireccional
    public void addProfesor(Profesor profesor) {
        profesores.add(profesor);
        profesor.setColegio(this);
    }

    public void removeProfesor(Profesor profesor) {
        profesores.remove(profesor);
        profesor.setColegio(null);
    }

    // Métodos similares para estudiantes y aulas


//    public void calcularPromedioColegio() {
//        if (aulas.isEmpty()) {
//            this.promedioColegio = 0.0;
//            return;
//        }
//
//        double sumaPromedios = aulas.stream()
//                .mapToDouble(Aula::getPromedioClases)
//                .sum();
//
//        this.promedioColegio = sumaPromedios / aulas.size();
//    }

    public void calcularRankingEstudiantes() {
        estudiantes.sort(Comparator.comparing(Estudiante::getPromedio).reversed());

        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante estudiante = estudiantes.get(i);
            estudiante.setRankEnColegio(i + 1);
        }
    }

    public void calcularRankingAulas() {
        aulas.sort(Comparator.comparing(Aula::getPromedioClases).reversed());

        for (int i = 0; i < aulas.size(); i++) {
            Aula aula = aulas.get(i);
            aula.setRank(i + 1);
        }
    }

    // Método para recalcular todos los promedios y rankings
    public void actualizarEstadisticas() {
        calcularPromedioColegio();
        calcularRankingEstudiantes();
        calcularRankingAulas();
        aulas.forEach(Aula::calcularRankingEstudiantes);
    }

    public void actualizarPromedio(double promedioAnterior, double promedioNuevo) {
        sumaPromedios = sumaPromedios - promedioAnterior + promedioNuevo;
        calcularPromedioColegio();
    }

    public void agregarEstudiante(double promedio) {
        sumaPromedios += promedio;
        contadorEstudiantes++;
        calcularPromedioColegio();
    }

    public void eliminarEstudiante(double promedio) {
        sumaPromedios -= promedio;
        contadorEstudiantes--;
        calcularPromedioColegio();
    }

    private void calcularPromedioColegio() {
        if (contadorEstudiantes > 0) {
            this.promedioColegio = sumaPromedios / contadorEstudiantes;
        } else {
            this.promedioColegio = 0.0;
        }
    }

}



