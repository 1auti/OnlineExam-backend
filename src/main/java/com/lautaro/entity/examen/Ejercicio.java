package com.lautaro.entity.examen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import com.lautaro.entity.examen.enums.Dificultad;
import com.lautaro.entity.examen.enums.TipoEjercicio;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ejercicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Ejercicio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String enunciado;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Opcion> opciones;

    @Enumerated(EnumType.STRING)
    private TipoEjercicio tipoEjercicio;

    @PositiveOrZero(message = "El puntaje máximo debe ser un número positivo o cero")
    private Double puntajeMaximo;

    @PositiveOrZero(message = "El puntaje obtenido debe ser un número positivo o cero")
    private Double puntajeObtenido;

    @Enumerated(EnumType.STRING)
    private Dificultad dificultad;

    @ManyToOne
    @JoinColumn(name = "examen_id")
    @JsonIgnore
    private Examen examen; // Relación muchos a uno con Examen

    public void agregarOpcion(Opcion opcion){
        opciones.add(opcion);
        opcion.setEjercicio(this);
        recalcularPuntajeObtenido();
    }

    private void recalcularPuntajeObtenido() {
        double puntajeObtenido = 0;
        for (Opcion opcion : opciones) {
            if (opcion.isEsCorrecta()) {
                puntajeObtenido += this.puntajeMaximo / opciones.stream().filter(Opcion::isEsCorrecta).count();
            }
        }
        this.puntajeObtenido = puntajeObtenido;
    }

}
