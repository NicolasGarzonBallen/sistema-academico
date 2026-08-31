package com.sistema.academico.sistema_academico.dto.materia;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** codigo y grupo no se incluyen: forman la PK y no se modifican vía PATCH. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaPatchDTO {

    @Size(max = 100)
    private String nombre;

    @Min(value = 3, message = "los créditos deben estar entre 3 y 4")
    @Max(value = 4, message = "los créditos deben estar entre 3 y 4")
    private Integer creditos;

    private Boolean estado;
}
