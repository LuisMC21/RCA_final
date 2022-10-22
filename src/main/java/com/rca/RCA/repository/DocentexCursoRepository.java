package com.rca.RCA.repository;

import com.rca.RCA.entity.DocentexCursoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocentexCursoRepository extends JpaRepository<DocentexCursoEntity, Integer> {

    //Función para contar las aulas existentes y activas de un grado, con filtro de código y nombre
     @Query(value = "SELECT count(x) from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%'))")
    Long findCountDocentexCurso(String status, String filter);

    //Función para listar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%'))")
    Optional<List<DocentexCursoEntity>> findDocentexCurso(String status, String filter, Pageable pageable);

    //Función para obtener un aula con su Identificado Único
    Optional<DocentexCursoEntity> findByUniqueIdentifier(String uniqueIdentifier);
}
