package com.sistema.academico.sistema_academico.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Clave compuesta de Materia: (codigo, grupo).
 * Debe ser Serializable y sobreescribir equals/hashCode (Lombok se encarga con @EqualsAndHashCode).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MateriaId implements Serializable {

    @Column(name = "codigo", length = 20, nullable = false)
    private String codigo;

    @Column(name = "grupo", length = 10, nullable = false)
    private String grupo;
}
