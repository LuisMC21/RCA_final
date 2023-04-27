package com.rca.RCA.repository;

import com.rca.RCA.entity.GradoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradoRepository extends JpaRepository<GradoEntity, Integer> {

    //Función para listar los grados activos con filro de código o nombre
    @Query(value = "select c from GradoEntity c " +
            "where c.status = :status " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Optional<List<GradoEntity>> findGrados(String status, String filter, Pageable pageable);

    //Función para contar los grados activos con filro de código o nombre
    @Query(value = "select count(c) from GradoEntity c " +
            "where c.status = :status " +
            "and (c.code like concat('%', :filter, '%') or c.name like concat('%', :filter, '%'))"+
            "order by c.name")
    Long findCountGrados(String status, String filter);

    //Función para obtener un grado con su Identificado Único
    @Query(value = "select g from GradoEntity g " +
            "where g.status = :status " +
            "and g.uniqueIdentifier = :id ")
    Optional<GradoEntity> findByUniqueIdentifier(String id, String status);
    //Función para obtener un grado con su nombre
    Optional<GradoEntity> findByName(Character name);
    @Query(value = "select count(g)>0 from GradoEntity g " +
            "where g.status = :status " +
            "and g.name = :name " +
            "and g.uniqueIdentifier != :id ")
    boolean existsByName(String status, Character name, String id);


}

