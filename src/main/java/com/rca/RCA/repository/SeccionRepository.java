package com.rca.RCA.repository;

import com.rca.RCA.entity.SeccionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeccionRepository extends JpaRepository<SeccionEntity, Integer> {

    //Función para listar las seccioens activass con filro de código o nombre
    @Query(value = "select s from SeccionEntity s " +
            "where s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))"+
            "order by s.name")
    Optional<List<SeccionEntity>> findSeccion(String status, String filter, Pageable pageable);

    //Función para contar las secciones activass con filro de código o nombre
    @Query(value = "select count(s) from SeccionEntity s " +
            "where s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))"+
            "order by s.name")
    Long findCountSeccion(String status, String filter);

    //Función para obtener una sección con su Identificado Único
    Optional<SeccionEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Función para obtener una sección con su nombre
    Optional<SeccionEntity> findByName(Character name);
/*
    //Función para contar las secciones existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT count(s) FROM GradoEntity g JOIN g.secciones s " +
            "WHERE g.uniqueIdentifier = :id " +
            "and s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))" +
            "order by s.name")
    Long findCountSeccionxGrado(String id, String status, String filter);

    //Función para listar las secciones existentes y activas de un grado, con filtro de código y nombre
    @Query(value = "SELECT s FROM GradoEntity g JOIN g.secciones s " +
            "WHERE g.uniqueIdentifier = :id " +
            "and s.status = :status " +
            "and (s.code like concat('%', :filter, '%') or s.name like concat('%', :filter, '%'))" +
            "order by s.name")
    Optional<List<SeccionEntity>> findSeccionxGrado(String id, String status, String filter, Pageable pageable);
*/
}

