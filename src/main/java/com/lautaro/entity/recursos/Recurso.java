package com.lautaro.entity.recursos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lautaro.common.BaseEntity;
import com.lautaro.entity.materia.Materia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "recursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Recurso extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Formato formato; // Ej: "video", "pdf", "imagen"
    private String ruta; // Ruta del archivo en el sistema de archivos
    @Lob
    @Column(name = "archivo", nullable = true)
    private byte[] archivo; // Almacena el contenido del archivo

    @ManyToOne
    @JoinColumn(name = "materia_id")
    @JsonIgnore
    private Materia materia;

    public boolean esFormatoValido() {
        return Formato.VIDEO.equals(this.formato) ||
                Formato.PDF.equals(this.formato) ||
                Formato.IMAGEN.equals(this.formato);
    }
}
