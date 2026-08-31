package com.sistema.academico.sistema_academico.service;

import com.sistema.academico.sistema_academico.dto.common.PageResponseDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudiantePatchDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudianteRequestDTO;
import com.sistema.academico.sistema_academico.dto.estudiante.EstudianteResponseDTO;
import com.sistema.academico.sistema_academico.entity.Estudiante;
import com.sistema.academico.sistema_academico.exception.DuplicateResourceException;
import com.sistema.academico.sistema_academico.exception.ResourceNotFoundException;
import com.sistema.academico.sistema_academico.repository.EstudianteRepository;
import com.sistema.academico.sistema_academico.specification.EstudianteSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    // Whitelist de campos por los que sí se puede ordenar (evita exponer nombres internos arbitrarios)
    private static final Set<String> CAMPOS_ORDENABLES = Set.of(
            "codigo", "nombre", "correo", "fechaIngreso", "fechaNacimiento", "estado", "carrera"
    );
    private static final String CAMPO_ORDEN_DEFECTO = "codigo";

    @Transactional(readOnly = true)
    public PageResponseDTO<EstudianteResponseDTO> listar(
            int page, int size, String sortBy, String sortDir,
            String nombre, String correo, String carrera, Boolean estado,
            LocalDate fechaNacimientoDesde, LocalDate fechaNacimientoHasta,
            LocalDate fechaIngresoDesde, LocalDate fechaIngresoHasta
    ) {
        Pageable pageable = construirPageable(page, size, sortBy, sortDir);

        Specification<Estudiante> spec = EstudianteSpecification.conFiltros(
                nombre, correo, carrera, estado,
                fechaNacimientoDesde, fechaNacimientoHasta,
                fechaIngresoDesde, fechaIngresoHasta
        );

        Page<Estudiante> resultado = estudianteRepository.findAll(spec, pageable);
        Page<EstudianteResponseDTO> resultadoDTO = resultado.map(this::toResponseDTO);
        return PageResponseDTO.from(resultadoDTO);
    }

    private Pageable construirPageable(int page, int size, String sortBy, String sortDir) {
        if (page < 0) {
            throw new IllegalArgumentException("el parámetro 'page' no puede ser negativo");
        }
        if (size < 1 || size > 200) {
            throw new IllegalArgumentException("el parámetro 'size' debe estar entre 1 y 200");
        }

        String campoOrden = (sortBy == null || sortBy.isBlank()) ? CAMPO_ORDEN_DEFECTO : sortBy;
        if (!CAMPOS_ORDENABLES.contains(campoOrden)) {
            throw new IllegalArgumentException(
                    "el campo de ordenamiento '" + sortBy + "' no es válido. Valores permitidos: " + CAMPOS_ORDENABLES);
        }

        Sort.Direction direccion = ("desc".equalsIgnoreCase(sortDir)) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direccion, campoOrden));
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO obtenerPorCodigo(String codigo) {
        Estudiante estudiante = buscarOFallar(codigo);
        return toResponseDTO(estudiante);
    }

    public EstudianteResponseDTO crear(EstudianteRequestDTO dto) {
        if (estudianteRepository.existsById(dto.getCodigo())) {
            throw new DuplicateResourceException("ya existe un estudiante con el código " + dto.getCodigo());
        }
        if (estudianteRepository.existsByCorreo(dto.getCorreo())) {
            throw new DuplicateResourceException("ya existe un estudiante con el correo " + dto.getCorreo());
        }

        Estudiante estudiante = Estudiante.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .correo(dto.getCorreo())
                .fechaIngreso(dto.getFechaIngreso())
                .fechaNacimiento(dto.getFechaNacimiento())
                .estado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado())
                .carrera(dto.getCarrera())
                .build();

        return toResponseDTO(estudianteRepository.save(estudiante));
    }

    // PUT: reemplazo completo del recurso existente
    public EstudianteResponseDTO reemplazar(String codigo, EstudianteRequestDTO dto) {
        Estudiante estudiante = buscarOFallar(codigo);

        if (!estudiante.getCorreo().equalsIgnoreCase(dto.getCorreo())
                && estudianteRepository.existsByCorreoAndCodigoNot(dto.getCorreo(), codigo)) {
            throw new DuplicateResourceException("ya existe un estudiante con el correo " + dto.getCorreo());
        }

        estudiante.setNombre(dto.getNombre());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setFechaIngreso(dto.getFechaIngreso());
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setEstado(dto.getEstado() == null ? Boolean.TRUE : dto.getEstado());
        estudiante.setCarrera(dto.getCarrera());

        return toResponseDTO(estudianteRepository.save(estudiante));
    }

    // PATCH: actualización parcial, solo se tocan los campos no nulos del DTO
    public EstudianteResponseDTO actualizarParcial(String codigo, EstudiantePatchDTO dto) {
        Estudiante estudiante = buscarOFallar(codigo);

        if (dto.getNombre() != null) {
            estudiante.setNombre(dto.getNombre());
        }
        if (dto.getCorreo() != null) {
            if (!estudiante.getCorreo().equalsIgnoreCase(dto.getCorreo())
                    && estudianteRepository.existsByCorreoAndCodigoNot(dto.getCorreo(), codigo)) {
                throw new DuplicateResourceException("ya existe un estudiante con el correo " + dto.getCorreo());
            }
            estudiante.setCorreo(dto.getCorreo());
        }
        if (dto.getFechaIngreso() != null) {
            estudiante.setFechaIngreso(dto.getFechaIngreso());
        }
        if (dto.getFechaNacimiento() != null) {
            estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        }
        if (dto.getEstado() != null) {
            estudiante.setEstado(dto.getEstado());
        }
        if (dto.getCarrera() != null) {
            estudiante.setCarrera(dto.getCarrera());
        }

        return toResponseDTO(estudianteRepository.save(estudiante));
    }

    public void eliminar(String codigo) {
        Estudiante estudiante = buscarOFallar(codigo);
        estudianteRepository.delete(estudiante);
    }

    private Estudiante buscarOFallar(String codigo) {
        return estudianteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("no existe un estudiante con el código " + codigo));
    }

    private EstudianteResponseDTO toResponseDTO(Estudiante e) {
        return EstudianteResponseDTO.builder()
                .codigo(e.getCodigo())
                .nombre(e.getNombre())
                .correo(e.getCorreo())
                .fechaIngreso(e.getFechaIngreso())
                .fechaNacimiento(e.getFechaNacimiento())
                .estado(e.getEstado())
                .carrera(e.getCarrera())
                .build();
    }

    // Usado por el DataLoader para saber cuántos estudiantes hay antes de poblar
    @Transactional(readOnly = true)
    public long contar() {
        return estudianteRepository.count();
    }

    public List<Estudiante> guardarTodos(List<Estudiante> estudiantes) {
        return estudianteRepository.saveAll(estudiantes);
    }
}
