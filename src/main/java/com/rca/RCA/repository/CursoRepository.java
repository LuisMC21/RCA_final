package com.rca.RCA.repository;

import com.rca.RCA.entity.CursoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<CursoEntity, Integer> {

    //Función para listar las seccioens activass con filro de código o nombre
    @Query(value = "select c from CursoEntity c " +
            "where c.status = :status " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Optional<List<CursoEntity>> findCurso(String status, String filter, Pageable pageable);

    //Función para contar las secciones activass con filro de código o nombre
    @Query(value = "select count(c) from CursoEntity c " +
            "where c.status = :status " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Long findCountCurso(String status, String filter);
    @Query(value = "select c from CursoEntity c " +
            "JOIN c.docentexCursoEntities dxc " +
            "JOIN dxc.aulaEntity au " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.anio_lectivoEntity anio " +
            "JOIN m.alumnoEntity alumno " +
            "where c.status = :status " +
            "AND alumno.uniqueIdentifier = :alumno " +
            "AND anio.uniqueIdentifier = :anio " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Optional<List<CursoEntity>> findCurso(String status, String filter, String anio, String alumno, Pageable pageable);

    @Query(value = "select count(c) from CursoEntity c " +
            "JOIN c.docentexCursoEntities dxc " +
            "JOIN dxc.aulaEntity au " +
            "JOIN au.matriculaEntities m " +
            "JOIN m.anio_lectivoEntity anio " +
            "JOIN m.alumnoEntity alumno " +
            "where c.status = :status " +
            "AND alumno.uniqueIdentifier = :alumno " +
            "AND anio.uniqueIdentifier = :anio " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Long findCountCurso(String status, String filter, String anio, String alumno);

    //Cursos por año y aula
    @Query(value = "SELECT c FROM DocentexCursoEntity dxc " +
            "JOIN dxc.cursoEntity c " +
            "JOIN dxc.aulaEntity au " +
            "JOIN dxc.anio_lectivoEntity anio " +
            "where c.status = :status " +
            "AND dxc.status = :status " +
            "AND au.status = :status " +
            "AND anio.status = :status " +
            "AND au.uniqueIdentifier = :aula " +
            "AND anio.uniqueIdentifier = :anio " +
            "order by c.name")
    Optional<List<CursoEntity>> findCursoByAulaAnio(String status, String aula, String anio);


    //Función para obtener una sección con su Identificado Único
    @Query(value = "SELECT c FROM CursoEntity c " +
            "WHERE c.uniqueIdentifier = :id " +
            "AND c.status = :status")
    Optional<CursoEntity> findByUniqueIdentifier(String id, String status);

    //Función para obtener una sección con su nombre

    @Query(value = "SELECT count(c)>0 FROM CursoEntity c " +
            "WHERE c.uniqueIdentifier != :id " +
            "AND c.status = :status " +
            "AND c.name = :name ")
    boolean existsByName(String name, String id, String status);
}