package com.sistema.academico.sistema_academico.dto.estudiante;

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
public class EstudianteResponseDTO {
    private String codigo;
    private String nombre;
    private String correo;
    private LocalDate fechaIngreso;
    private LocalDate fechaNacimiento;
    private Boolean estado;
    private String carrera;
}
