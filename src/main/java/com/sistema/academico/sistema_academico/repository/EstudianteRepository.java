package com.sistema.academico.sistema_academico.repository;

import com.sistema.academico.sistema_academico.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JpaSpecificationExecutor permite combinar filtros dinámicos (ver EstudianteSpecification)
 * junto con la paginación y el ordenamiento que ya trae JpaRepository.
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String>,
        JpaSpecificationExecutor<Estudiante> {

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndCodigoNot(String correo, String codigo);
}
