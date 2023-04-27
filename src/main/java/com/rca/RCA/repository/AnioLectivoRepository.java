package com.rca.RCA.repository;

import com.rca.RCA.entity.AnioLectivoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnioLectivoRepository extends JpaRepository<AnioLectivoEntity, Integer> {

    //Función para listar los años lectivos activass con filtro de código o nombre
    @Query(value = "select a from AnioLectivoEntity a " +
            "where a.status = :status " +
            "and (a.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%'))"+
            "order by a.name")
    Optional<List<AnioLectivoEntity>> findAnioLectivo(String status, String filter, Pageable pageable);

    //Función para contar las secciones activass con filro de código o nombre
    @Query(value = "select count(a) from AnioLectivoEntity a " +
            "where a.status = :status " +
            "and (a.code like concat('%', :filter, '%') or a.name like concat('%', :filter, '%'))"+
            "order by a.name")
    Long findCountSeccion(String status, String filter);

    //Función para obtener una sección con su Identificado Único
    @Query(value = "SELECT a FROM AnioLectivoEntity a " +
            "WHERE a.status = :status " +
            "AND a.uniqueIdentifier = :id ")
    Optional<AnioLectivoEntity> findByUniqueIdentifier(String id, String status);

    //Función para obtener una sección con su nombre
    @Query(value = "SELECT count(a)>0 FROM AnioLectivoEntity a " +
            "WHERE a.name = :name " +
            "AND a.status = :status " +
            "AND a.uniqueIdentifier != :id ")
    boolean existsByName(String name, String status, String id);

    Optional<AnioLectivoEntity> findByName(String name);
}

