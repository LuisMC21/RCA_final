package com.rca.RCA.repository;

import com.rca.RCA.entity.PeriodoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoEntity, Integer> {

    //Función para listar los periodos activos con filro de código o nombre
    @Query(value = "select p from PeriodoEntity p " +
            "where p.status = :status " +
            "and (p.code like concat('%', :filter, '%') or p.name like concat('%', :filter, '%'))"+
            "order by p.name")
    Optional<List<PeriodoEntity>> findPeriodo(String status, String filter, Pageable pageable);

    //Función para contar los periodos activos con filro de código o nombre
    @Query(value = "select count(p) from PeriodoEntity p " +
            "where p.status = :status " +
            "and (p.code like concat('%', :filter, '%') or p.name like concat('%', :filter, '%'))"+
            "order by p.name")
    Long findCountPeriodo(String status, String filter);

    //Función para obtener un periodo con su Identificado Único
    Optional<PeriodoEntity> findByUniqueIdentifier(String uniqueIdentifier);

    //Función para obtener un periodo con su nombre
    @Query(value = "SELECT x from AnioLectivoEntity a " +
            "JOIN a.periodoEntities x " +
            "WHERE a=x.anio_lectivoEntity " +
            "AND a.uniqueIdentifier = :id_anioLectivo " +
            "AND x.name = :name " +
            "AND x.status = :status " +
            "AND a.status= :status ")
    Optional<PeriodoEntity> findByName(String id_anioLectivo, String name, String status);
    @Query(value = "SELECT x from AnioLectivoEntity a " +
            "JOIN a.periodoEntities x " +
            "WHERE a=x.anio_lectivoEntity " +
            "AND a.id = :id_anioLectivo " +
            "AND x.status = :status " +
            "AND a.status= :status ")
    Optional<List<PeriodoEntity>> findById_AnioLectivo(Integer id_anioLectivo, String status);
}

