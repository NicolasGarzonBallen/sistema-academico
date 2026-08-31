package com.sistema.academico.sistema_academico.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "materia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {

    @EmbeddedId
    private MateriaId id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "creditos", nullable = false)
    private Integer creditos;

    @Column(name = "estado")
    @Builder.Default
    private Boolean estado = true;
}
