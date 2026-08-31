package com.sistema.academico.sistema_academico.controller;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudiantePatchDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudianteRequestDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudianteResponseDTO;
import com.sistema.academico.sistema_academico.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "CRUD de estudiantes, con paginación, ordenamiento y filtros")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @GetMapping
    @Operation(summary = "Lista estudiantes con paginación, ordenamiento y filtros combinables",
            description = "Soporta ?page, ?size, ?sortBy, ?sortDir y filtros: nombre, correo, carrera, estado, "
                    + "fechaNacimientoDesde/Hasta, fechaIngresoDesde/Hasta. Todos los filtros son opcionales y combinables.")
    public ResponseEntity<PageResponseDTO<EstudianteResponseDTO>> listar(
            @Parameter(description = "Número de página, empieza en 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página (1-200)") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo de ordenamiento: codigo, nombre, correo, fechaIngreso, fechaNacimiento, estado, carrera")
            @RequestParam(defaultValue = "codigo") String sortBy,
            @Parameter(description = "Dirección de orden: asc o desc") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Filtro parcial (contiene) por nombre") @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtro parcial (contiene) por correo") @RequestParam(required = false) String correo,
            @Parameter(description = "Filtro parcial (contiene) por carrera") @RequestParam(required = false) String carrera,
            @Parameter(description = "Filtro exacto por estado (true/false)") @RequestParam(required = false) Boolean estado,
            @Parameter(description = "Fecha de nacimiento mínima (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimientoDesde,
            @Parameter(description = "Fecha de nacimiento máxima (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimientoHasta,
            @Parameter(description = "Fecha de ingreso mínima (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIngresoDesde,
            @Parameter(description = "Fecha de ingreso máxima (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIngresoHasta
    ) {
        PageResponseDTO<EstudianteResponseDTO> resultado = estudianteService.listar(
                page, size, sortBy, sortDir,
                nombre, correo, carrera, estado,
                fechaNacimientoDesde, fechaNacimientoHasta,
                fechaIngresoDesde, fechaIngresoHasta
        );
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtiene un estudiante por su código")
    public ResponseEntity<EstudianteResponseDTO> obtener(@PathVariable String codigo) {
        return ResponseEntity.ok(estudianteService.obtenerPorCodigo(codigo));
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo estudiante")
    public ResponseEntity<EstudianteResponseDTO> crear(@Valid @RequestBody EstudianteRequestDTO dto) {
        EstudianteResponseDTO creado = estudianteService.crear(dto);
        return ResponseEntity.created(URI.create("/estudiantes/" + creado.getCodigo())).body(creado);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Reemplaza por completo un estudiante existente")
    public ResponseEntity<EstudianteResponseDTO> reemplazar(
            @PathVariable String codigo, @Valid @RequestBody EstudianteRequestDTO dto) {
        return ResponseEntity.ok(estudianteService.reemplazar(codigo, dto));
    }

    @PatchMapping("/{codigo}")
    @Operation(summary = "Actualiza parcialmente uno o varios campos de un estudiante")
    public ResponseEntity<EstudianteResponseDTO> actualizarParcial(
            @PathVariable String codigo, @Valid @RequestBody EstudiantePatchDTO dto) {
        return ResponseEntity.ok(estudianteService.actualizarParcial(codigo, dto));
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Elimina un estudiante")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        estudianteService.eliminar(codigo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
