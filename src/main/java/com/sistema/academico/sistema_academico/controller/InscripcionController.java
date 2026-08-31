package com.sistema.academico.sistema_academico.controller;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionPatchDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionRequestDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionResponseDTO;
import com.sistema.academico.sistema_academico.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "CRUD de inscripciones (relación estudiante-materia)")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @GetMapping
    @Operation(summary = "Lista inscripciones con paginación")
    public ResponseEntity<PageResponseDTO<InscripcionResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(inscripcionService.listar(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una inscripción por su id")
    public ResponseEntity<InscripcionResponseDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(inscripcionService.obtener(id));
    }

    @PostMapping
    @Operation(summary = "Crea una nueva inscripción (valida que el estudiante y la materia existan)")
    public ResponseEntity<InscripcionResponseDTO> crear(@Valid @RequestBody InscripcionRequestDTO dto) {
        InscripcionResponseDTO creada = inscripcionService.crear(dto);
        return ResponseEntity.created(URI.create("/inscripciones/" + creada.getId())).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplaza por completo una inscripción existente")
    public ResponseEntity<InscripcionResponseDTO> reemplazar(
            @PathVariable Integer id, @Valid @RequestBody InscripcionRequestDTO dto) {
        return ResponseEntity.ok(inscripcionService.reemplazar(id, dto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza parcialmente una inscripción (fecha y/o estado)")
    public ResponseEntity<InscripcionResponseDTO> actualizarParcial(
            @PathVariable Integer id, @Valid @RequestBody InscripcionPatchDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una inscripción")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
