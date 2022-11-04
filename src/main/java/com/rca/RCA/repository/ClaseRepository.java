package com.rca.RCA.repository;

import com.rca.RCA.entity.ClaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<ClaseEntity, Integer> {

/*
    @Query(value = "SELECT c FROM ClaseEntity c " +
            "WHERE c.status = :status " +
            "AND ( c.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    @Query(value = "SELECT count(c) FROM ClaseEntity c " +
            "WHERE c.status = :status " +
            "AND ( c.code like concat('%', :filter, '%'))")
*/
    //Función para listar las clases según el codigo de docentexCurso
    @Query(value = "select c from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status and dc.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Optional<List<ClaseEntity>> findEntities(String status, String filter, Pageable pageable);

    //Función para contar las clases
    @Query(value = "select count(c) from ClaseEntity c JOIN c.docentexCursoEntity dc WHERE dc = c.docentexCursoEntity and " +
            "c.status = :status and dc.status = :status " +
            "and ( dc.code like concat('%', :filter, '%'))")
    Long findCountEntities(String status, String filter);

    Optional<ClaseEntity> findByUniqueIdentifier(String uniqueIdentifier);

    @Query(value = "SELECT c FROM AulaEntity a " +
            "JOIN a.claseEntities c " +
            "WHERE a=c.aulaEntity " +
            "AND a.uniqueIdentifier = :id_aula " +
            "AND a.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findByAula(String id_aula, String status);
    @Query(value = "SELECT c FROM PeriodoEntity p " +
            "JOIN p.claseEntities c " +
            "WHERE p=c.periodoEntity " +
            "AND p.uniqueIdentifier = :id_periodo " +
            "AND p.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findById_Periodo(String id_periodo, String status);
    @Query(value = "SELECT c FROM DocentexCursoEntity d " +
            "JOIN d.claseEntities c " +
            "WHERE d=c.docentexCursoEntity " +
            "AND d.uniqueIdentifier = :id_dxc " +
            "AND d.status = :status " +
            "AND c.status= :status ")
    Optional<List<ClaseEntity>> findById_DxC(String id_dxc, String status);

    //Función para eliminar las asistencias asociadas a una clase
    @Transactional
    @Modifying
    @Query(value = "update asistencia a JOIN clase c  SET a.tx_status = 'DELETED', a.tx_delete_at = :fecha " +
            "where a.clase_id = c.id " +
            "and c.tx_unique_identifier = :uniqueIdentifier", nativeQuery = true)
    void eliminarAsistenias(@Param("uniqueIdentifier") String uniqueIdentifier, @Param("fecha") LocalDateTime fecha);

}
