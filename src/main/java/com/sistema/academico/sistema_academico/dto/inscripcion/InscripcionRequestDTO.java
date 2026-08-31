package com.sistema.academico.sistema_academico.dto.inscripcion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionRequestDTO {

    @NotBlank(message = "el código del estudiante es obligatorio")
    private String codigoEstudiante;

    @NotBlank(message = "el código de la materia es obligatorio")
    private String codigoMateria;

    @NotBlank(message = "el grupo de la materia es obligatorio")
    private String grupoMateria;

    @NotNull(message = "la fecha de inscripción es obligatoria")
    @PastOrPresent(message = "la fecha de inscripción no puede ser futura")
    private LocalDate fechaInscripcion;

    private Boolean estado;
}
