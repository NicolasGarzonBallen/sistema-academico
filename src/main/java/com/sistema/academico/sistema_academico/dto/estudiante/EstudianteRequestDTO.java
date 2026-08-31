package com.sistema.academico.sistema_academico.dto.estudiante;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Usado en POST (crear) y PUT (reemplazo completo). Todos los campos son obligatorios. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteRequestDTO {

    @NotBlank(message = "el código es obligatorio")
    @Size(max = 20, message = "el código no puede superar 20 caracteres")
    private String codigo;

    @NotBlank(message = "el nombre es obligatorio")
    @Size(max = 100, message = "el nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "el correo es obligatorio")
    @Email(message = "el correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    @NotNull(message = "la fecha de ingreso es obligatoria")
    @PastOrPresent(message = "la fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    @NotNull(message = "la fecha de nacimiento es obligatoria")
    @Past(message = "la fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private Boolean estado;

    @NotBlank(message = "la carrera es obligatoria")
    @Size(max = 100)
    private String carrera;
}
