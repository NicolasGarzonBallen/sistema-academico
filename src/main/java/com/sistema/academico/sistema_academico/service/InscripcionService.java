package com.sistema.academico.sistema_academico.service;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionPatchDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionRequestDTO;
import com.sistema.academico.sistema_academico.dto.inscripcion.InscripcionResponseDTO;
import com.sistema.academico.sistema_academico.entity.Estudiante;
import com.sistema.academico.sistema_academico.entity.Inscripcion;
import com.sistema.academico.sistema_academico.entity.Materia;
import com.sistema.academico.sistema_academico.entity.MateriaId;
import com.sistema.academico.sistema_academico.exception.BusinessRuleException;
import com.sistema.academico.sistema_academico.exception.ResourceNotFoundException;
import com.sistema.academico.sistema_academico.repository.EstudianteRepository;
import com.sistema.academico.sistema_academico.repository.InscripcionRepository;
import com.sistema.academico.sistema_academico.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    @Transactional(readOnly = true)
    public PageResponseDTO<InscripcionResponseDTO> listar(int page, int size) {
        if (page < 0) throw new IllegalArgumentException("el parámetro 'page' no puede ser negativo");
        if (size < 1 || size > 200) throw new IllegalArgumentException("el parámetro 'size' debe estar entre 1 y 200");

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Inscripcion> resultado = inscripcionRepository.findAll(pageable);
        return PageResponseDTO.from(resultado.map(this::toResponseDTO));
    }

    @Transactional(readOnly = true)
    public InscripcionResponseDTO obtener(Integer id) {
        return toResponseDTO(buscarOFallar(id));
    }

    public InscripcionResponseDTO crear(InscripcionRequestDTO dto) {
        Estudiante estudiante = buscarEstudiante(dto.getCodigoEstudiante());
        Materia materia = buscarMateria(dto.getCodigoMateria(), dto.getGrupoMateria());

        if (Boolean.FALSE.equals(estudiante.getEstado())) {
            throw new BusinessRuleException("el estudiante " + estudiante.getCodigo() + " está inactivo");
        }
        if (Boolean.FALSE.equals(materia.getEstado())) {
            throw new BusinessRuleException("la materia " + materia.getId().getCodigo() + " está inactiva");
        }

        Inscripcion inscripcion = Inscripcion.builder()
                .estudiante(estudiante)
                .materia(materia)
                .fechaInscripcion(dto.getFechaInscripcion())
                .estado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado())
                .build();

        return toResponseDTO(inscripcionRepository.save(inscripcion));
    }

    public InscripcionResponseDTO reemplazar(Integer id, InscripcionRequestDTO dto) {
        Inscripcion inscripcion = buscarOFallar(id);
        Estudiante estudiante = buscarEstudiante(dto.getCodigoEstudiante());
        Materia materia = buscarMateria(dto.getCodigoMateria(), dto.getGrupoMateria());

        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(dto.getFechaInscripcion());
        inscripcion.setEstado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado());

        return toResponseDTO(inscripcionRepository.save(inscripcion));
    }

    public InscripcionResponseDTO actualizarParcial(Integer id, InscripcionPatchDTO dto) {
        Inscripcion inscripcion = buscarOFallar(id);
        if (dto.getFechaInscripcion() != null) {
            inscripcion.setFechaInscripcion(dto.getFechaInscripcion());
        }
        if (dto.getEstado() != null) {
            inscripcion.setEstado(dto.getEstado());
        }
        return toResponseDTO(inscripcionRepository.save(inscripcion));
    }

    public void eliminar(Integer id) {
        Inscripcion inscripcion = buscarOFallar(id);
        inscripcionRepository.delete(inscripcion);
    }

    private Inscripcion buscarOFallar(Integer id) {
        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no existe una inscripción con el id " + id));
    }

    private Estudiante buscarEstudiante(String codigo) {
        return estudianteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("no existe un estudiante con el código " + codigo));
    }

    private Materia buscarMateria(String codigo, String grupo) {
        return materiaRepository.findById(new MateriaId(codigo, grupo))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "no existe la materia " + codigo + " grupo " + grupo));
    }

    private InscripcionResponseDTO toResponseDTO(Inscripcion i) {
        return InscripcionResponseDTO.builder()
                .id(i.getId())
                .codigoEstudiante(i.getEstudiante().getCodigo())
                .nombreEstudiante(i.getEstudiante().getNombre())
                .codigoMateria(i.getMateria().getId().getCodigo())
                .grupoMateria(i.getMateria().getId().getGrupo())
                .nombreMateria(i.getMateria().getNombre())
                .fechaInscripcion(i.getFechaInscripcion())
                .estado(i.getEstado())
                .build();
    }
}
