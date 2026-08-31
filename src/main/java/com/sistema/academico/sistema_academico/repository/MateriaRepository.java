package com.sistema.academico.sistema_academico.repository;

import com.sistema.academico.sistema_academico.entity.Materia;
import com.sistema.academico.sistema_academico.entity.MateriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, MateriaId> {
}
