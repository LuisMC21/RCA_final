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
            "JOIN x.anio_lectivoEntity a " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND a.uniqueIdentifier = :anio " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%'))")
    Long findCountDocentexCurso(String status, String anio, String filter);

    //Función para listar las aulas existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT x from DocenteEntity d " +
            "JOIN d.docentexCursoEntities x " +
            "JOIN x.cursoEntity c " +
            "JOIN x.anio_lectivoEntity a " +
            "WHERE d=x.docenteEntity " +
            "AND d.status = :status " +
            "AND x.status = :status " +
            "AND c.status = :status " +
            "AND a.uniqueIdentifier = :anio " +
            "AND (d.code like concat('%', :filter, '%') or x.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%')) " +
            "order by x.aulaEntity.gradoEntity.name, x.aulaEntity.seccionEntity.name")
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
    @Query(value = "SELECT a from AulaEntity a " +
            "JOIN a.docentexCursoEntities x " +
            "WHERE a=x.aulaEntity " +
            "AND a.uniqueIdentifier = :id_aula " +
            "AND x.status = :status " +
            "AND a.status= :status ")
    Optional<List<DocentexCursoEntity>> findByAula(String id_aula, String status);
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

    @Query(value = "SELECT count(dxc)>0 FROM DocentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity c " +
            "JOIN dxc.aulaEntity a " +
            "JOIN dxc.docenteEntity d " +
            "WHERE dxc.uniqueIdentifier != :id " +
            "AND dxc.status = :status " +
            "AND d.uniqueIdentifier = :idD " +
            "AND c.uniqueIdentifier = :idC " +
            "AND a.uniqueIdentifier = :idA ")
    boolean existsByDocenteCursoAula(String id, String idD, String idC, String idA, String status);
}
