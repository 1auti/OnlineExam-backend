package com.lautaro.entity.examen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "opciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Opcion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String texto;

    private boolean esCorrecta;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id")
    @JsonIgnore
    private Ejercicio ejercicio;


}
