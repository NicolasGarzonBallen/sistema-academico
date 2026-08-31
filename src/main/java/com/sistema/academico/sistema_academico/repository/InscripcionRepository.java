package com.sistema.academico.sistema_academico.repository;

import com.sistema.academico.sistema_academico.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
}
