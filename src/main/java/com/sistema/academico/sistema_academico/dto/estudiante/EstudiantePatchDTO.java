package com.sistema.academico.sistema_academico.dto.estudiante;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Usado en PATCH: todos los campos son opcionales (nulos = "no tocar ese campo").
 * El código no se incluye porque es la PK y no debe cambiar vía PATCH.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudiantePatchDTO {

    @Size(max = 100, message = "el nombre no puede superar 100 caracteres")
    private String nombre;

    @Email(message = "el correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    @PastOrPresent(message = "la fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    @Past(message = "la fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private Boolean estado;

    @Size(max = 100)
    private String carrera;
}
