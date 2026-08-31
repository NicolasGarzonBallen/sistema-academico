package com.sistema.academico.sistema_academico.service;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaPatchDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaRequestDTO;
import com.sistema.academico.sistema_academico.dto.materia.MateriaResponseDTO;
import com.sistema.academico.sistema_academico.entity.Materia;
import com.sistema.academico.sistema_academico.entity.MateriaId;
import com.sistema.academico.sistema_academico.exception.DuplicateResourceException;
import com.sistema.academico.sistema_academico.exception.ResourceNotFoundException;
import com.sistema.academico.sistema_academico.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MateriaService {

    private final MateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public PageResponseDTO<MateriaResponseDTO> listar(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("el parámetro 'page' no puede ser negativo");
        if (size < 1 || size > 200) throw new IllegalArgumentException("el parámetro 'size' debe estar entre 1 y 200");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nombre"));
        Page<Materia> resultado = materiaRepository.findAll(pageable);
        return PageResponseDTO.from(resultado.map(this::toResponseDTO));
    }

    @Transactional(readOnly = true)
    public MateriaResponseDTO obtener(String codigo, String grupo) {
        return toResponseDTO(buscarOFallar(codigo, grupo));
    }

    public MateriaResponseDTO crear(MateriaRequestDTO dto) {
        MateriaId id = new MateriaId(dto.getCodigo(), dto.getGrupo());
        if (materiaRepository.existsById(id)) {
            throw new DuplicateResourceException(
                    "ya existe la materia " + dto.getCodigo() + " grupo " + dto.getGrupo());
        }
        Materia materia = Materia.builder()
                .id(id)
                .nombre(dto.getNombre())
                .creditos(dto.getCreditos())
                .estado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado())
                .build();
        return toResponseDTO(materiaRepository.save(materia));
    }

    public MateriaResponseDTO reemplazar(String codigo, String grupo, MateriaRequestDTO dto) {
        Materia materia = buscarOFallar(codigo, grupo);
        materia.setNombre(dto.getNombre());
        materia.setCreditos(dto.getCreditos());
        materia.setEstado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado());
        return toResponseDTO(materiaRepository.save(materia));
    }

    public MateriaResponseDTO actualizarParcial(String codigo, String grupo, MateriaPatchDTO dto) {
        Materia materia = buscarOFallar(codigo, grupo);
        if (dto.getNombre() != null) materia.setNombre(dto.getNombre());
        if (dto.getCreditos() != null) materia.setCreditos(dto.getCreditos());
        if (dto.getEstado() != null) materia.setEstado(dto.getEstado());
        return toResponseDTO(materiaRepository.save(materia));
    }

    public void eliminar(String codigo, String grupo) {
        Materia materia = buscarOFallar(codigo, grupo);
        materiaRepository.delete(materia);
    }

    private Materia buscarOFallar(String codigo, String grupo) {
        return materiaRepository.findById(new MateriaId(codigo, grupo))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "no existe la materia " + codigo + " grupo " + grupo));
    }

    private MateriaResponseDTO toResponseDTO(Materia m) {
        return MateriaResponseDTO.builder()
                .codigo(m.getId().getCodigo())
                .grupo(m.getId().getGrupo())
                .nombre(m.getNombre())
                .creditos(m.getCreditos())
                .estado(m.getEstado())
                .build();
    }

    @Transactional(readOnly = true)
    public long contar() {
        return materiaRepository.count();
    }

    public List<Materia> guardarTodos(List<Materia> materias) {
        return materiaRepository.saveAll(materias);
    }
}
