package com.sistema.academico.sistema_academico.dto.inscripcion;

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
public class InscripcionResponseDTO {
    private Integer id;
    private String codigoEstudiante;
    private String nombreEstudiante;
    private String codigoMateria;
    private String grupoMateria;
    private String nombreMateria;
    private LocalDate fechaInscripcion;
    private Boolean estado;
}
