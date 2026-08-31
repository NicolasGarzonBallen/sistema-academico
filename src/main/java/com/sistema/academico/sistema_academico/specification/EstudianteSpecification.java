package com.sistema.academico.sistema_academico.specification;

import com.sistema.academico.sistema_academico.entity.Estudiante;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Construye un Specification<Estudiante> combinando solo los filtros que
 * realmente llegaron en la petición (los nulos se ignoran). Esto permite
 * que el GET /estudiantes soporte cualquier combinación de: nombre, correo,
 * carrera, estado y rangos de fecha de nacimiento / ingreso.
 */
public class EstudianteSpecification {

    private EstudianteSpecification() {
    }

    public static Specification<Estudiante> conFiltros(
            String nombre,
            String correo,
            String carrera,
            Boolean estado,
            LocalDate fechaNacimientoDesde,
            LocalDate fechaNacimientoHasta,
            LocalDate fechaIngresoDesde,
            LocalDate fechaIngresoHasta
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }
            if (correo != null && !correo.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("correo")), "%" + correo.toLowerCase() + "%"));
            }
            if (carrera != null && !carrera.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("carrera")), "%" + carrera.toLowerCase() + "%"));
            }
            if (estado != null) {
                predicates.add(cb.equal(root.get("estado"), estado));
            }
            if (fechaNacimientoDesde != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), fechaNacimientoDesde));
            }
            if (fechaNacimientoHasta != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaNacimiento"), fechaNacimientoHasta));
            }
            if (fechaIngresoDesde != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaIngreso"), fechaIngresoDesde));
            }
            if (fechaIngresoHasta != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaIngreso"), fechaIngresoHasta));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
