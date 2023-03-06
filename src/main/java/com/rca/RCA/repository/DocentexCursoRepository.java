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
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND d.id = :id_docente " +
            "AND x.status = :status " +
            "AND d.status= :status ")
    Optional<List<DocentexCursoEntity>> findByDocente(Integer id_docente, String status);
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND c.id = :id_curso " +
            "AND x.status = :status " +
            "AND c.status= :status ")
    Optional<List<DocentexCursoEntity>> findByCurso(Integer id_curso, String status);

    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "WHERE d=x.docenteEntity " +
            "AND c=x.cursoEntity " +
            "AND d.id = :id_docente " +
            "AND c.id = :id_curso " +
            "AND x.status = :status " +
            "AND d.status = :status " +
            "AND c.status= :status ")
    Optional<List<DocentexCursoEntity>> findByDocenteYCurso(Integer id_docente, Integer id_curso, String status);
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.gradoEntity g " +
            "WHERE d=x.docenteEntity " +
            "AND g.id = :id_grado " +
            "AND x.status = :status " +
            "AND g.status= :status ")
    Optional<List<DocentexCursoEntity>> findById_Grado(Integer id_grado, String status);
}
