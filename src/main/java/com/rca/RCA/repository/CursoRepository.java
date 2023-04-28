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