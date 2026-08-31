package com.sistema.academico.sistema_academico.dto.materia;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class MateriaRequestDTO {

    @NotBlank(message = "el código es obligatorio")
    @Size(max = 20)
    private String codigo;

    @NotBlank(message = "el grupo es obligatorio")
    @Size(max = 10)
    private String grupo;

    @NotBlank(message = "el nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "los créditos son obligatorios")
    @Min(value = 3, message = "los créditos deben estar entre 3 y 4")
    @Max(value = 4, message = "los créditos deben estar entre 3 y 4")
    private Integer creditos;

    private Boolean estado;
}
