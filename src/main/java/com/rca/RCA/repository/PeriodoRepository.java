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

    //Función para listar los periodos activos con filro de código o nombre o por anio lectivo
    @Query(value = "select p from PeriodoEntity p " +
            "where " +
            "p.status = :status " +
            "and (p.code like concat('%', :filter, '%') or p.name like concat('%', :filter, '%') or p.anio_lectivoEntity.uniqueIdentifier like concat('%', :filter, '%') " +
            "or p.anio_lectivoEntity.name like concat('%', :filter, '%')) "+
            "order by p.name")
    Optional<List<PeriodoEntity>> findPeriodo(String status, String filter, Pageable pageable);


    //Función para contar los periodos activos con filro de código o nombre
    @Query(value = "select count(p) from PeriodoEntity p " +
            "where p.status = :status " +
            "and (p.code like concat('%', :filter, '%') or p.name like concat('%', :filter, '%') or p.anio_lectivoEntity.uniqueIdentifier like concat('%', :filter, '%')) "+
            "order by p.name")
    Long findCountPeriodo(String status, String filter);

    //Función para obtener un periodo con su Identificado Único

    @Query(value = "SELECT p from PeriodoEntity p " +
            "WHERE p.status = :status " +
            "AND p.uniqueIdentifier = :id ")
    Optional<PeriodoEntity> findByUniqueIdentifier(String id, String status);

    //Función para obtener un periodo con su nombre
    @Query(value = "SELECT count(x)>0 from AnioLectivoEntity a " +
            "JOIN a.periodoEntities x " +
            "WHERE a=x.anio_lectivoEntity " +
            "AND a.uniqueIdentifier = :idAL " +
            "AND x.status = :status " +
            "AND x.name = :name " +
            "AND x.uniqueIdentifier != :id ")
    boolean existsByName (String id, String idAL, String name, String status);
    @Query(value = "SELECT x from AnioLectivoEntity a " +
            "JOIN a.periodoEntities x " +
            "WHERE a=x.anio_lectivoEntity " +
            "AND a.uniqueIdentifier = :id_anioLectivo " +
            "AND x.status = :status " +
            "AND a.status= :status ")
    Optional<List<PeriodoEntity>> findById_AnioLectivo(String id_anioLectivo, String status);
}


