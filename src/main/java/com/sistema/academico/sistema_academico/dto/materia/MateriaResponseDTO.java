package com.sistema.academico.sistema_academico.dto.materia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaResponseDTO {
    private String codigo;
    private String grupo;
    private String nombre;
    private Integer creditos;
    private Boolean estado;
}
