package com.sistema.academico.sistema_academico.dto.inscripcion;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Solo se permite corregir la fecha o el estado; el estudiante/materia de una inscripción no cambian vía PATCH. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionPatchDTO {

    @PastOrPresent(message = "la fecha de inscripción no puede ser futura")
    private LocalDate fechaInscripcion;

    private Boolean estado;
}
