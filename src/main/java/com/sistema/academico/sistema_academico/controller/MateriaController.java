package com.sistema.academico.sistema_academico.controller;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaPatchDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaRequestDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaResponseDTO;
import com.sistema.academico.sistema_academico.service.MateriaService;
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

/**
 * La PK de Materia es compuesta (codigo, grupo), así que se expone como dos
 * segmentos de ruta: /materias/{codigo}/{grupo}
 */
@RestController
@RequestMapping("/materias")
@RequiredArgsConstructor
@Tag(name = "Materias", description = "CRUD de materias")
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping
    @Operation(summary = "Lista materias con paginación")
    public ResponseEntity<PageResponseDTO<MateriaResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(materiaService.listar(page, size));
    }

    @GetMapping("/{codigo}/{grupo}")
    @Operation(summary = "Obtiene una materia por código y grupo")
    public ResponseEntity<MateriaResponseDTO> obtener(
            @PathVariable String codigo, @PathVariable String grupo) {
        return ResponseEntity.ok(materiaService.obtener(codigo, grupo));
    }

    @PostMapping
    @Operation(summary = "Crea una nueva materia")
    public ResponseEntity<MateriaResponseDTO> crear(@Valid @RequestBody MateriaRequestDTO dto) {
        MateriaResponseDTO creada = materiaService.crear(dto);
        return ResponseEntity.created(URI.create("/materias/" + creada.getCodigo() + "/" + creada.getGrupo()))
                .body(creada);
    }

    @PutMapping("/{codigo}/{grupo}")
    @Operation(summary = "Reemplaza por completo una materia existente")
    public ResponseEntity<MateriaResponseDTO> reemplazar(
            @PathVariable String codigo, @PathVariable String grupo, @Valid @RequestBody MateriaRequestDTO dto) {
        return ResponseEntity.ok(materiaService.reemplazar(codigo, grupo, dto));
    }

    @PatchMapping("/{codigo}/{grupo}")
    @Operation(summary = "Actualiza parcialmente una materia")
    public ResponseEntity<MateriaResponseDTO> actualizarParcial(
            @PathVariable String codigo, @PathVariable String grupo, @Valid @RequestBody MateriaPatchDTO dto) {
        return ResponseEntity.ok(materiaService.actualizarParcial(codigo, grupo, dto));
    }

    @DeleteMapping("/{codigo}/{grupo}")
    @Operation(summary = "Elimina una materia")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo, @PathVariable String grupo) {
        materiaService.eliminar(codigo, grupo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
